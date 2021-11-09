package com.example.webdemo.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SingleArticleResponseDTO implements Serializable {

    @NotNull
    @JsonAlias("article")
    private ArticleResponseDTO article;
}
