package com.example.webdemo.service.impl;

import com.example.webdemo.dto.request.article.CommentDTO;
import com.example.webdemo.exception.ItemNotFoundException;
import com.example.webdemo.model.Article;
import com.example.webdemo.model.Comment;
import com.example.webdemo.model.User;
import com.example.webdemo.repo.ArticleRepository;
import com.example.webdemo.repo.CommentRepository;
import com.example.webdemo.service.ICommentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    Logger logger = LoggerFactory.getLogger(CommentService.class);

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(rollbackFor = Exception.class)
    public Optional<Comment> saveComment(CommentDTO commentDTO, User currentUser, String slug) {
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> {
            logger.error("Article does not exist!");
            return new ItemNotFoundException("Article does not exist!");
        });
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setAuthor(currentUser);
        comment.setArticle(article);
        article.getComments().add(comment);
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment with id {} saved successfully", savedComment.getId());
        return Optional.of(savedComment);
    }

    public List<Comment> getComments(String slug) {
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> new ItemNotFoundException("Article does not exist!"));
        return article.getComments();
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Article article, Long commentId) {
        List<Comment> comments = article.getComments().stream().filter(comment -> comment.getId().equals(commentId)).collect(Collectors.toList());
        if (!comments.isEmpty()) {
            article.getComments().remove(comments.get(0));
            articleRepository.save(article);
            logger.info("Comment with id {} deleted successfully", commentId);
        } else {
            logger.error("No comment to delete!");
            throw new ItemNotFoundException("No comment to delete!");
        }
    }

}
