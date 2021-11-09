package com.example.webdemo.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@JsonRootName("profile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private String username;

    private String bio;

    private String image;

    private boolean following;
}
