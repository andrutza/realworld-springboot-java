package com.example.webdemo.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("tags")
@Builder
public class TagsDTO implements Serializable {

    @NotNull
    private List<String> tags;

}
