package com.project.springboot.service;


import com.project.springboot.dto.PostDto;
import com.project.springboot.dto.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    PostResponse getAllPost(int pageNum, int pageSize,String sortBy,String sortDir);

    PostDto getPostByid(long id);

    PostDto updatePost(PostDto postDto, long id);

    void deletePostById(long id);

    List<PostDto> getPostByCategory(Long categoryId);

}
