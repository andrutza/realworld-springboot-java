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
public class MultipleCommentResponseDTO implements Serializable {

    @NotNull
    @JsonAlias("comments")
    private List<CommentResponseDTO> comments;
}
