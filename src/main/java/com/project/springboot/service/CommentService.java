package com.project.springboot.service;

import com.project.springboot.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostid(long postId);

    CommentDto getCommentById(Long postId, Long commentId);

    CommentDto updateComment(Long postId,Long commentId,CommentDto commentRequest);

    void deleteComment(Long postId,Long commentId);
}
