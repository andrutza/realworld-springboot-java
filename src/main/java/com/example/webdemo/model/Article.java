package com.example.webdemo.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "articles")
@Builder
public class Article implements Serializable {

    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "slug", length = 50)
    @EqualsAndHashCode.Include
    private String slug;

    @Column(name = "title", length = 50, nullable = false)
    @EqualsAndHashCode.Include
    private String title;

    @Column(name = "description", length = 1000, nullable = false)
    @EqualsAndHashCode.Include
    private String description;

    @Column(name = "body", length = 1000, nullable = false)
    @EqualsAndHashCode.Include
    private String body;

    @ElementCollection
    @CollectionTable(name = "article_tag_list")
    private List<String> tagList;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Include
    private User author;

    @JoinTable(name = "favorite_relation", joinColumns = {
            @JoinColumn(name = "favorite", referencedColumnName = "article_id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "favoriteBy", referencedColumnName = "user_id", nullable = false)})
    @ManyToMany()
    @EqualsAndHashCode.Exclude
    private List<User> favoriteByPersons = new ArrayList<>();

    @OneToMany(mappedBy = "article",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}
