package com.example.webdemo.dto.request.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO implements Serializable {

    @NotNull
    @JsonAlias("user")
    @Valid
    private UserDTO userDTO;
}
