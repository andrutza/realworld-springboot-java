package com.example.webdemo.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultipleArticleResponseDTO implements Serializable {

    @NotNull
    @JsonAlias("articles")
    private List<ArticleResponseDTO> articles;

    @NotNull
    private Integer articlesCount;
}
