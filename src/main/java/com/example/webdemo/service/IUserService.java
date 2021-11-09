package com.example.webdemo.service;

import com.example.webdemo.dto.request.user.UserDTO;
import com.example.webdemo.dto.request.user.UserWithDetailsDTO;
import com.example.webdemo.exception.ItemAlreadyExistsException;
import com.example.webdemo.exception.ItemNotFoundException;
import com.example.webdemo.model.User;

import java.util.Optional;

public interface IUserService {

    Optional<User> saveUser(UserDTO userDto) throws ItemAlreadyExistsException;

    Optional<User> updateUser(String email, UserWithDetailsDTO userDto) throws ItemNotFoundException;

    Optional<User> loadByUsername(String username);
}
