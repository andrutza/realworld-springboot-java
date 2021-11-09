package com.example.webdemo.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
@Builder
public class Comment implements Serializable {

    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "body", length = 1000, nullable = false)
    @EqualsAndHashCode.Include
    private String body;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Include
    private User author;

    @ManyToOne()
    @JoinColumn(name = "article_id")
    @EqualsAndHashCode.Include
    private Article article;

}
