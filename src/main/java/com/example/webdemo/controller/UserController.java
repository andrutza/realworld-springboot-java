package com.example.webdemo.controller;

import com.example.webdemo.dto.UserConverter;
import com.example.webdemo.dto.request.user.UserDTO;
import com.example.webdemo.dto.request.user.UserRequestDTO;
import com.example.webdemo.dto.request.user.UserUpdateRequestDTO;
import com.example.webdemo.dto.request.user.UserWithDetailsDTO;
import com.example.webdemo.dto.response.UserResponseDTO;
import com.example.webdemo.dto.response.UserWithTokenDTO;
import com.example.webdemo.model.User;
import com.example.webdemo.security.AuthenticationUtil;
import com.example.webdemo.service.IUserService;
import com.example.webdemo.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationUtil authenticationUtil;

    @RequestMapping(value = ("/users"), method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO requestDTO) {
        UserDTO userDTO = requestDTO.getUserDTO();
        Optional<User> user = userService.saveUser(userDTO);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (user.isPresent()) {
            final String jwt = authenticationUtil.generateJwtToken(user.get());
            UserWithTokenDTO userWithTokenDTO = userConverter.convertToDto(user.get());
            userWithTokenDTO.setToken(jwt);

            return ResponseEntity.ok(UserResponseDTO.builder().user(userWithTokenDTO).build());
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = ("/users/login"), method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> authenticateUser(@RequestBody UserRequestDTO requestDTO) {
        try {
            UserDTO userDTO = requestDTO.getUserDTO();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            final String jwt = authenticationUtil.generateJwtToken(user);

            UserWithTokenDTO userWithTokenDTO = userConverter.convertToDto(user);
            userWithTokenDTO.setToken(jwt);

            return ResponseEntity.ok(UserResponseDTO.builder().user(userWithTokenDTO).build());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @RequestMapping(value = ("/user"), method = RequestMethod.GET)
    public ResponseEntity<UserResponseDTO> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserWithTokenDTO userWithTokenDTO = userConverter.convertToDto(user);
        userWithTokenDTO.setToken(authentication.getDetails().toString());

        return ResponseEntity.ok(UserResponseDTO.builder().user(userWithTokenDTO).build());
    }

    @RequestMapping(value = ("/user"), method = RequestMethod.PUT)
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserUpdateRequestDTO requestDTO) {
        UserWithDetailsDTO userDTO = requestDTO.getUserDTO();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Optional<User> savedUser = userService.updateUser(user.getEmail(), userDTO);

        if (savedUser.isPresent()) {
            UserWithTokenDTO userWithTokenDTO = userConverter.convertToDto(savedUser.get());

            final String jwt = authenticationUtil.generateJwtToken(savedUser.get());
            userWithTokenDTO.setToken(jwt);

            return ResponseEntity.ok(UserResponseDTO.builder().user(userWithTokenDTO).build());
        }

        return ResponseEntity.notFound().build();
    }
}
