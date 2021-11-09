package com.example.webdemo.service;

import com.example.webdemo.dto.request.article.ArticleDTO;
import com.example.webdemo.dto.request.article.ArticleWithTagsDTO;
import com.example.webdemo.exception.ItemAlreadyExistsException;
import com.example.webdemo.exception.ItemNotFoundException;
import com.example.webdemo.model.Article;
import com.example.webdemo.model.User;

import java.util.List;
import java.util.Optional;

public interface IArticleService {

    Optional<Article> saveArticle(ArticleWithTagsDTO articleDTO, User currentUser) throws ItemAlreadyExistsException;

    Optional<Article> updateArticle(Article article, ArticleDTO articleDTO);

    Optional<Article> findArticle(String slug) throws ItemNotFoundException;

    List<Article> getArticles(String tag, String author, String favorited, Integer limit, Integer offset) throws ItemNotFoundException;

    List<Article> getArticles(String username, Integer limit, Integer offset) throws ItemNotFoundException;

    void deleteArticle(String slug);

    List<String> getTags();
}
