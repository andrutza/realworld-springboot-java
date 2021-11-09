package com.example.webdemo.dto.response;

import com.example.webdemo.model.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    @Autowired
    private ModelMapper modelMapper;

    public CommentResponseDTO convertToDto(Comment comment) {
        return modelMapper.map(comment, CommentResponseDTO.class);
    }
}
