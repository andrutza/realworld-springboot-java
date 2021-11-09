package com.example.webdemo.service;

import com.example.webdemo.dto.request.article.CommentDTO;
import com.example.webdemo.exception.ItemAlreadyExistsException;
import com.example.webdemo.exception.ItemNotFoundException;
import com.example.webdemo.model.Article;
import com.example.webdemo.model.Comment;
import com.example.webdemo.model.User;

import java.util.List;
import java.util.Optional;

public interface ICommentService {

    Optional<Comment> saveComment(CommentDTO commentDTO, User currentUser, String slug) throws ItemAlreadyExistsException;

    List<Comment> getComments(String slug) throws ItemNotFoundException;

    void deleteComment(Article article, Long commentId);
}
