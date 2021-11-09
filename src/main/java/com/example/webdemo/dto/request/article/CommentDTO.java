package com.example.webdemo.dto.request.article;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("comment")
@Builder
public class CommentDTO implements Serializable {

    @NotBlank
    private String body;

}
