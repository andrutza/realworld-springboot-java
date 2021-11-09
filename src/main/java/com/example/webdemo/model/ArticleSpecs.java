package com.example.webdemo.model;

import org.springframework.data.jpa.domain.Specification;

public final class ArticleSpecs {

    public static Specification<Article> getArticlesByTagSpec(String tag) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isMember(tag, root.get(Article_.tagList));
    }

    public static Specification<Article> getArticlesByAuthorSpec(String author) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Article_.author).get(User_.username), author);
    }
}
