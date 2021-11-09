package com.example.webdemo.controller;

import com.example.webdemo.controller.common.AuthUtils;
import com.example.webdemo.dto.request.article.CommentDTO;
import com.example.webdemo.dto.request.article.CommentRequestDTO;
import com.example.webdemo.dto.response.*;
import com.example.webdemo.model.Article;
import com.example.webdemo.model.Comment;
import com.example.webdemo.model.User;
import com.example.webdemo.service.IArticleService;
import com.example.webdemo.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/articles/{slug}/comments")
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private CommentConverter commentConverter;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<SingleCommentResponseDTO> createComment(@PathVariable String slug, @Valid @RequestBody CommentRequestDTO requestDTO) {
        CommentDTO commentDTO = requestDTO.getCommentDTO();

        User currentUser = AuthUtils.getCurrentUser();
        Optional<Comment> comment = commentService.saveComment(commentDTO, currentUser, slug);

        if (comment.isPresent()) {
            CommentResponseDTO commentResponseDTO = commentConverter.convertToDto(comment.get());

            return ResponseEntity.ok(SingleCommentResponseDTO.builder().comment(commentResponseDTO).build());
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MultipleCommentResponseDTO> getComments(@PathVariable String slug) {

        List<Comment> comments = commentService.getComments(slug);

        List<CommentResponseDTO> commentResponseDTOList = comments.stream()
                .map(comment -> commentConverter.convertToDto(comment))
                .collect(Collectors.toList());
        MultipleCommentResponseDTO multipleCommentResponseDTO = MultipleCommentResponseDTO.builder()
                .comments(commentResponseDTOList)
                .build();
        return ResponseEntity.ok(multipleCommentResponseDTO);
    }

    @RequestMapping(value = ("/{id}"), method = RequestMethod.DELETE)
    public ResponseEntity<SingleArticleResponseDTO> deleteComment(@PathVariable String slug, @PathVariable String id) {
        Optional<Article> article = articleService.findArticle(slug);

        User currentUser = AuthUtils.getCurrentUser();

        if (article.isPresent()) {
            if (!article.get().getAuthor().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                commentService.deleteComment(article.get(), Long.valueOf(id));
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }


}
