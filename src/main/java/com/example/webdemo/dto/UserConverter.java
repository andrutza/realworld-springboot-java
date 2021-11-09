package com.example.webdemo.dto;

import com.example.webdemo.dto.response.ProfileDTO;
import com.example.webdemo.dto.response.UserWithTokenDTO;
import com.example.webdemo.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private ModelMapper modelMapper;

    public UserWithTokenDTO convertToDto(User user) {
        return modelMapper.map(user, UserWithTokenDTO.class);
    }

    public ProfileDTO convertToProfileDto(User user) {
        return modelMapper.map(user, ProfileDTO.class);
    }
}
