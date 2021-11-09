package com.example.webdemo.service.impl;

import com.example.webdemo.model.Article;
import com.example.webdemo.model.User;
import com.example.webdemo.repo.ArticleRepository;
import com.example.webdemo.service.IFavoriteArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FavoriteArticleService implements IFavoriteArticleService {

    Logger logger = LoggerFactory.getLogger(FavoriteArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional(rollbackFor = Exception.class)
    public Optional<Article> saveFavoriteRelation(Article article, User user) {
        article.getFavoriteByPersons().add(user);
        Article savedArticle = articleRepository.save(article);
        logger.info("Saved favorite relation for article with slug {}", savedArticle.getSlug());
        return Optional.of(savedArticle);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Article> deleteFavoriteRelation(Article article, User user) {
        article.getFavoriteByPersons().remove(user);
        Article savedArticle = articleRepository.save(article);
        logger.info("Deleted favorite relation for article with slug {}", savedArticle.getSlug());
        return Optional.of(savedArticle);
    }

}
