package com.example.webdemo.service;

import com.example.webdemo.model.Article;
import com.example.webdemo.model.User;

import java.util.Optional;

public interface IFavoriteArticleService {

    Optional<Article> saveFavoriteRelation(Article article, User user);

    Optional<Article> deleteFavoriteRelation(Article article, User user);
}
