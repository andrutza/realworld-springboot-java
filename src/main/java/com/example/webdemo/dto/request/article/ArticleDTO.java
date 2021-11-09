package com.example.webdemo.dto.request.article;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("article")
@Builder
public class ArticleDTO implements Serializable {

    private String title;

    private String description;

    private String body;

}
