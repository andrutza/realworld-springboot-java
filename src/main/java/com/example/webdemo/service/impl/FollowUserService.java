package com.example.webdemo.service.impl;

import com.example.webdemo.model.User;
import com.example.webdemo.repo.UserRepository;
import com.example.webdemo.service.IFollowUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FollowUserService implements IFollowUserService {

    Logger logger = LoggerFactory.getLogger(FollowUserService.class);

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public Optional<User> saveFollowRelation(User user, User followedUser) {
        followedUser.getFollowers().add(user);
        User savedUser = userRepository.save(followedUser);
        logger.info("Saved follow relation for user with id {}", savedUser.getId());
        return Optional.of(savedUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<User> deleteFollowRelation(User user, User followedUser) {
        followedUser.getFollowers().remove(user);
        User savedUser = userRepository.save(followedUser);
        logger.info("Deleted follow relation for user with id {}", savedUser.getId());
        return Optional.of(savedUser);
    }


}
