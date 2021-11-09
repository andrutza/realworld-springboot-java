package com.example.webdemo.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "users")
@Builder
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    @EqualsAndHashCode.Include
    private String username;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String password;

    @Column(name = "email", length = 50, nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "description", length = 1000)
    @EqualsAndHashCode.Include
    private String bio;

    @Column(name = "image", length = 50)
    @EqualsAndHashCode.Include
    private String image;

    @OneToMany(mappedBy = "author")
    private List<Article> articles;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JoinTable(name = "follow_relation", joinColumns = {
            @JoinColumn(name = "followed", referencedColumnName = "user_id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "followedBy", referencedColumnName = "user_id", nullable = false)})
    @ManyToMany()
    private List<User> followers = new ArrayList<>();

    @ManyToMany(mappedBy = "followers")
    private List<User> followedPersons = new ArrayList<>();

    @ManyToMany(mappedBy = "favoriteByPersons")
    private List<Article> favoriteArticles = new ArrayList<>();
}
