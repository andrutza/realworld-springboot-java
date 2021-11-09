package com.example.webdemo.controller;

import com.example.webdemo.controller.common.AuthUtils;
import com.example.webdemo.dto.UserConverter;
import com.example.webdemo.dto.response.ProfileDTO;
import com.example.webdemo.dto.response.ProfileResponseDTO;
import com.example.webdemo.model.User;
import com.example.webdemo.service.IFollowUserService;
import com.example.webdemo.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/profiles/{username}")
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IFollowUserService followUserService;
    @Autowired
    private UserConverter userConverter;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ProfileResponseDTO> getUser(@PathVariable String username) {
        Optional<User> user = userService.loadByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = AuthUtils.getCurrentUser();

        ProfileDTO profileDTO = getProfileDTO(user.get(), currentUser);

        return ResponseEntity.ok(ProfileResponseDTO.builder().profile(profileDTO).build());
    }

    @RequestMapping(value = "/follow", method = RequestMethod.POST)
    public ResponseEntity<ProfileResponseDTO> followUser(@PathVariable String username) {
        Optional<User> user = userService.loadByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = AuthUtils.getCurrentUser();

        ProfileDTO profileDTO = new ProfileDTO();
        if (!user.get().getFollowers().contains(currentUser)) {
            Optional<User> savedUser = followUserService.saveFollowRelation(currentUser, user.get());
            if (savedUser.isPresent()) {
                profileDTO = getProfileDTO(savedUser.get(), currentUser);
            }
        } else {
            profileDTO = getProfileDTO(user.get(), currentUser);
        }
        return ResponseEntity.ok(ProfileResponseDTO.builder().profile(profileDTO).build());
    }

    @RequestMapping(value = "/follow", method = RequestMethod.DELETE)
    public ResponseEntity<ProfileResponseDTO> unfollowUser(@PathVariable String username) {
        Optional<User> user = userService.loadByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = AuthUtils.getCurrentUser();

        ProfileDTO profileDTO = new ProfileDTO();
        if (user.get().getFollowers().contains(currentUser)) {
            Optional<User> savedUser = followUserService.deleteFollowRelation(currentUser, user.get());
            if (savedUser.isPresent()) {
                profileDTO = getProfileDTO(savedUser.get(), currentUser);
            }
        } else {
            profileDTO = getProfileDTO(user.get(), currentUser);
        }
        return ResponseEntity.ok(ProfileResponseDTO.builder().profile(profileDTO).build());
    }

    private ProfileDTO getProfileDTO(User user, User currentUser) {
        ProfileDTO profileDTO = userConverter.convertToProfileDto(user);
        if (user.getFollowers().contains(currentUser)) {
            profileDTO.setFollowing(true);
        } else {
            profileDTO.setFollowing(false);
        }
        return profileDTO;
    }
}
