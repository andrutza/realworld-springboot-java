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
public class UserWithDetailsDTO implements Serializable {


    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", message = "Email be valid")
    private String email;

    private String username;
    
    private String password;

    private String bio;

    private String image;
}
