package com.example.webdemo.dto.request.article;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDTO implements Serializable {

    @NotNull
    @JsonAlias("comment")
    @Valid
    private CommentDTO commentDTO;
}
