package com.example.webdemo.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonRootName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithTokenDTO {

    @NotNull
    private String email;

    @NotNull
    private String username;

    @NotNull
    private String token;

    private String bio;

    private String image;
}
