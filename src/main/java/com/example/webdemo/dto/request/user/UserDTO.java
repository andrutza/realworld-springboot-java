package com.example.webdemo.dto.request.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
@Builder
public class UserDTO implements Serializable {

    @NotNull
    @Pattern(regexp = "^(.+)@(\\S+)$", message = "Email must be valid")
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;
}
