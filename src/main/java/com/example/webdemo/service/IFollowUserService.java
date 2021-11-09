package com.example.webdemo.service;

import com.example.webdemo.model.User;

import java.util.Optional;

public interface IFollowUserService {

    Optional<User> saveFollowRelation(User user, User followedUser);

    Optional<User> deleteFollowRelation(User user, User followedUser);
}
