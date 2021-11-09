package com.example.webdemo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleResponseDTO implements Serializable {

    @NotNull
    private String slug;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String body;

    private List<String> tagList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date updatedAt;

    private boolean favorited;

    private int favoritesCount;

    @NotNull
    private ProfileDTO author;

}
