package com.example.webdemo.controller;

import com.example.webdemo.dto.response.TagsDTO;
import com.example.webdemo.service.IArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    @Autowired
    private IArticleService articleService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<TagsDTO> getTags() {
        List<String> tags = articleService.getTags();

        return ResponseEntity.ok(TagsDTO.builder().tags(tags).build());
    }

}
