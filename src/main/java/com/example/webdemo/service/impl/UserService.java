package com.example.webdemo.service.impl;

import com.example.webdemo.dto.request.user.UserDTO;
import com.example.webdemo.dto.request.user.UserWithDetailsDTO;
import com.example.webdemo.exception.ItemAlreadyExistsException;
import com.example.webdemo.exception.ItemNotFoundException;
import com.example.webdemo.model.User;
import com.example.webdemo.repo.UserRepository;
import com.example.webdemo.service.IUserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService, IUserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(rollbackFor = Exception.class)
    public Optional<User> saveUser(UserDTO userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            logger.error("User already exists!");
            throw new ItemAlreadyExistsException("User already exists!");
        });
        User savedUser = userRepository.save(user);
        logger.info("User with id {} saved successfully", savedUser.getId());
        return Optional.of(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<User> updateUser(String email, UserWithDetailsDTO userDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            logger.error("User not found!");
            return new ItemNotFoundException("User not found!");
        });
        modelMapper.map(userDto, user);

        if (userDto.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        User savedUser = userRepository.save(user);
        logger.info("User with id {} updated successfully", savedUser.getId());
        return Optional.of(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> {
            logger.error("User not found!");
            return new ItemNotFoundException("User not found!");
        });
    }

    public Optional<User> loadByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.error("User not found!");
            return new ItemNotFoundException("User not found!");
        });
        return Optional.of(user);
    }

}
