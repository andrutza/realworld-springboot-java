package com.example.webdemo.service.impl;

import com.example.webdemo.dto.request.article.ArticleDTO;
import com.example.webdemo.dto.request.article.ArticleWithTagsDTO;
import com.example.webdemo.exception.ItemAlreadyExistsException;
import com.example.webdemo.exception.ItemNotFoundException;
import com.example.webdemo.model.Article;
import com.example.webdemo.model.ArticleSpecs;
import com.example.webdemo.model.User;
import com.example.webdemo.repo.ArticleRepository;
import com.example.webdemo.repo.UserRepository;
import com.example.webdemo.service.IArticleService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService implements IArticleService {

    Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(rollbackFor = Exception.class)
    public Optional<Article> saveArticle(ArticleWithTagsDTO articleDTO, User currentUser) {
        Article article = modelMapper.map(articleDTO, Article.class);
        article.setSlug(articleDTO.getTitle().replace(" ", "-").toLowerCase());
        article.setAuthor(currentUser);
        articleRepository.findBySlug(article.getSlug()).ifPresent(a -> {
            logger.error("Article already exists!");
            throw new ItemAlreadyExistsException("Article already exists!");
        });
        Article savedArticle = articleRepository.save(article);
        logger.info("Article with slug {} saved successfully", savedArticle.getSlug());
        return Optional.of(savedArticle);
    }

    @Transactional(rollbackFor = Exception.class)
    public Optional<Article> updateArticle(Article article, ArticleDTO articleDTO) {
        modelMapper.map(articleDTO, article);
        if (articleDTO.getTitle() != null) {
            article.setSlug(articleDTO.getTitle().replace(" ", "-").toLowerCase());
        }
        Article savedArticle = articleRepository.save(article);
        logger.info("Article with slug {} updated successfully", savedArticle.getSlug());
        return Optional.of(savedArticle);
    }

    public Optional<Article> findArticle(String slug) {
        Article article = articleRepository.findBySlug(slug).orElseThrow(() -> {
            logger.error("Article not found!");
            return new ItemNotFoundException("Article not found!");
        });
        return Optional.of(article);
    }

    public List<Article> getArticles(String tag, String author, String favorited, Integer limit, Integer offset) {
        List<Article> articles = new ArrayList<>();
        Pageable page = getCreatedAt(limit, offset);

        if (favorited != null) {
            Optional<User> user = userRepository.findByUsername(favorited);
            if (user.isPresent()) {
                articles = user.get().getFavoriteArticles();
                articles = new PageImpl<>(articles, page, articles.size()).getContent();
            }
        } else if (tag != null || author != null) {
            articles = articleRepository.findAll(Specification.where(ArticleSpecs.getArticlesByTagSpec(tag))
                    .or(ArticleSpecs.getArticlesByAuthorSpec(author)), page)
                    .getContent();
        } else {
            articles = articleRepository.findAll(page).getContent();
        }

        if (articles.isEmpty()) {
            logger.error("No articles found!");
            throw new ItemNotFoundException("No articles found!");
        }
        logger.info("Found {} articles", articles.size());
        return articles;
    }

    public List<Article> getArticles(String username, Integer limit, Integer offset) {
        Pageable page = getCreatedAt(limit, offset);
        Optional<User> user = userRepository.findByUsername(username);
        List<Article> articles;
        if (user.isPresent()) {
            articles = user.get().getFollowedPersons().stream()
                    .map(User::getArticles)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            articles = new PageImpl<>(articles, page, articles.size()).getContent();
        } else {
            logger.error("No user found!");
            throw new ItemNotFoundException("No user found!");
        }
        logger.info("Found {} articles", articles.size());
        return articles;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(String slug) {
        Integer count = articleRepository.deleteBySlug(slug);
        if (count == 0) {
            logger.error("No article to delete!");
            throw new ItemNotFoundException("No article to delete!");
        }
    }

    public List<String> getTags() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            logger.error("No articles found!");
            throw new ItemNotFoundException("No articles found!");
        }
        logger.info("Found {} articles", articles.size());
        return articles
                .stream()
                .map(Article::getTagList)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private PageRequest getCreatedAt(Integer limit, Integer offset) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "createdAt").descending());
    }

}
