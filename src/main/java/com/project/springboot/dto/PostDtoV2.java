/*package com.project.springboot.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class PostDtoV2 {

    private Long id;
    @NotEmpty
    @Size(min = 2,message = "post title should have at least 2 characters")
    private String title;
    @NotEmpty
    @Size(min = 10, message = "post description should have at least 10 characters")
    private String description;
    @NotEmpty
    private String content;

    private Set<CommentDto> comments;

    private Long categoryId;

    private List<String> tags;

}*/
