package com.example.webdemo.controller;

import com.example.webdemo.controller.common.AuthUtils;
import com.example.webdemo.dto.ArticleConverter;
import com.example.webdemo.dto.request.article.ArticleRequestDTO;
import com.example.webdemo.dto.request.article.ArticleUpdateRequestDTO;
import com.example.webdemo.dto.request.article.ArticleWithTagsDTO;
import com.example.webdemo.dto.response.ArticleResponseDTO;
import com.example.webdemo.dto.response.MultipleArticleResponseDTO;
import com.example.webdemo.dto.response.SingleArticleResponseDTO;
import com.example.webdemo.model.Article;
import com.example.webdemo.model.User;
import com.example.webdemo.service.IArticleService;
import com.example.webdemo.service.impl.FavoriteArticleService;
import javassist.NotFoundException;
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
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    @Autowired
    private IArticleService articleService;
    @Autowired
    private FavoriteArticleService favoriteArticleService;
    @Autowired
    private ArticleConverter articleConverter;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<SingleArticleResponseDTO> createArticle(@Valid @RequestBody ArticleRequestDTO requestDTO) {
        ArticleWithTagsDTO articleDTO = requestDTO.getArticleDTO();

        User currentUser = AuthUtils.getCurrentUser();
        Optional<Article> article = articleService.saveArticle(articleDTO, currentUser);

        if (article.isPresent()) {
            ArticleResponseDTO articleResponseDTO = getArticleResponseDTO(currentUser, article.get());
            return ResponseEntity.ok(SingleArticleResponseDTO.builder().article(articleResponseDTO).build());
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = ("/{slug}"), method = RequestMethod.GET)
    public ResponseEntity<SingleArticleResponseDTO> getArticle(@PathVariable String slug) {
        Optional<Article> article = articleService.findArticle(slug);

        if (article.isPresent()) {
            ArticleResponseDTO articleResponseDTO = articleConverter.convertToDto(article.get());
            return ResponseEntity.ok(SingleArticleResponseDTO.builder().article(articleResponseDTO).build());
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<MultipleArticleResponseDTO> getArticles(@RequestParam(required = false) String tag,
                                                                  @RequestParam(required = false) String author,
                                                                  @RequestParam(required = false) String favorited,
                                                                  @RequestParam(required = false, defaultValue = "20") String limit,
                                                                  @RequestParam(required = false, defaultValue = "0") String offset) {

        List<Article> articles = articleService.getArticles(tag, author, favorited, Integer.valueOf(limit), Integer.valueOf(offset));

        User currentUser = AuthUtils.getCurrentUser();

        List<ArticleResponseDTO> articleResponseDTOList = articles.stream()
                .map(article -> getArticleResponseDTO(currentUser, article))
                .collect(Collectors.toList());
        MultipleArticleResponseDTO multipleArticleResponseDTO = MultipleArticleResponseDTO.builder()
                .articles(articleResponseDTOList)
                .articlesCount(articles.size())
                .build();
        return ResponseEntity.ok(multipleArticleResponseDTO);
    }

    @RequestMapping(value = ("/feed"), method = RequestMethod.GET)
    public ResponseEntity<MultipleArticleResponseDTO> getArticles(@RequestParam(required = false, defaultValue = "20") String limit,
                                                                  @RequestParam(required = false, defaultValue = "0") String offset) {

        User currentUser = AuthUtils.getCurrentUser();
        List<Article> articles = articleService.getArticles(currentUser.getUsername(), Integer.valueOf(limit), Integer.valueOf(offset));

        List<ArticleResponseDTO> articleResponseDTOList = articles.stream()
                .map(article -> getArticleResponseDTO(currentUser, article))
                .collect(Collectors.toList());
        MultipleArticleResponseDTO multipleArticleResponseDTO = MultipleArticleResponseDTO.builder()
                .articles(articleResponseDTOList)
                .articlesCount(articles.size())
                .build();
        return ResponseEntity.ok(multipleArticleResponseDTO);
    }

    @RequestMapping(value = ("/articles/{slug}"), method = RequestMethod.PUT)
    public ResponseEntity<SingleArticleResponseDTO> updateArticle(@PathVariable String slug, @RequestBody ArticleUpdateRequestDTO requestDTO) throws NotFoundException {
        Optional<Article> existingArticle = articleService.findArticle(slug);

        User currentUser = AuthUtils.getCurrentUser();
        if (existingArticle.isPresent()) {
            if (!existingArticle.get().getAuthor().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                Optional<Article> article = articleService.updateArticle(existingArticle.get(), requestDTO.getArticleDTO());

                if (article.isPresent()) {
                    ArticleResponseDTO articleResponseDTO = getArticleResponseDTO(currentUser, article.get());
                    return ResponseEntity.ok(SingleArticleResponseDTO.builder().article(articleResponseDTO).build());
                }
            }
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = ("/{slug}"), method = RequestMethod.DELETE)
    public ResponseEntity<SingleArticleResponseDTO> deleteArticle(@PathVariable String slug) {
        Optional<Article> existingArticle = articleService.findArticle(slug);
        User currentUser = AuthUtils.getCurrentUser();

        if (existingArticle.isPresent()) {
            if (!existingArticle.get().getAuthor().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                articleService.deleteArticle(slug);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/{slug}/favorite", method = RequestMethod.POST)
    public ResponseEntity<SingleArticleResponseDTO> favoriteArticle(@PathVariable String slug) {
        Optional<Article> article = articleService.findArticle(slug);

        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = AuthUtils.getCurrentUser();
        ArticleResponseDTO articleResponseDTO = new ArticleResponseDTO();
        if (!article.get().getFavoriteByPersons().contains(currentUser)) {
            Optional<Article> savedArticle = favoriteArticleService.saveFavoriteRelation(article.get(), currentUser);
            if (savedArticle.isPresent()) {
                articleResponseDTO = getArticleResponseDTO(currentUser, savedArticle.get());
            }
        } else {
            articleResponseDTO = getArticleResponseDTO(currentUser, article.get());
        }

        return ResponseEntity.ok(SingleArticleResponseDTO.builder().article(articleResponseDTO).build());
    }

    @RequestMapping(value = "/{slug}/favorite", method = RequestMethod.DELETE)
    public ResponseEntity<SingleArticleResponseDTO> unfavoriteArticle(@PathVariable String slug) {
        Optional<Article> article = articleService.findArticle(slug);

        if (article.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User currentUser = AuthUtils.getCurrentUser();

        ArticleResponseDTO articleResponseDTO = new ArticleResponseDTO();
        if (article.get().getFavoriteByPersons().contains(currentUser)) {
            Optional<Article> savedArticle = favoriteArticleService.deleteFavoriteRelation(article.get(), currentUser);
            if (savedArticle.isPresent()) {
                articleResponseDTO = getArticleResponseDTO(currentUser, savedArticle.get());
            }
        } else {
            articleResponseDTO = getArticleResponseDTO(currentUser, article.get());
        }
        return ResponseEntity.ok(SingleArticleResponseDTO.builder().article(articleResponseDTO).build());
    }

    private ArticleResponseDTO getArticleResponseDTO(User currentUser, Article article) {
        ArticleResponseDTO articleResponseDTO = articleConverter.convertToDto(article);
        if (article.getFavoriteByPersons().contains(currentUser)) {
            articleResponseDTO.setFavorited(true);
        } else {
            articleResponseDTO.setFavorited(false);
        }
        articleResponseDTO.setFavoritesCount(article.getFavoriteByPersons().size());
        if (article.getAuthor().getFollowers().contains(currentUser)) {
            articleResponseDTO.getAuthor().setFollowing(true);
        } else {
            articleResponseDTO.getAuthor().setFollowing(false);
        }
        return articleResponseDTO;
    }


}
