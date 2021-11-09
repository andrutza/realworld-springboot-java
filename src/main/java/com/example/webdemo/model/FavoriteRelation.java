package com.example.webdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorite_relation")
@Builder
@IdClass(FavoriteRelationId.class)
public class FavoriteRelation {

    @Id
    private Long favorite;

    @Id
    private Long favoriteBy;

}
