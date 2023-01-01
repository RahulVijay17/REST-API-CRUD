package com.project.springboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CommentDto {
    private long id;

    @NotEmpty(message = "name should not be null or empty")
    private String name;
    @NotEmpty(message = "email should not be null or empty")
    @Email
    private String email;
    @NotEmpty
    @Size(min = 10,message = "comment body should not be less than 10" )
    private String body;
}
