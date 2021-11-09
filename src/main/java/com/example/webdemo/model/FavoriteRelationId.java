package com.example.webdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteRelationId implements Serializable {

    private Long favorite;

    private Long favoriteBy;

}
