package com.example.webdemo.dto;

import com.example.webdemo.dto.response.ArticleResponseDTO;
import com.example.webdemo.model.Article;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {

    @Autowired
    private ModelMapper modelMapper;

    public ArticleResponseDTO convertToDto(Article article) {
        return modelMapper.map(article, ArticleResponseDTO.class);
    }
}
