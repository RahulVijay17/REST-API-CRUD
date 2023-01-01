package com.project.springboot.controller;

import com.project.springboot.dto.PostDto;
import com.project.springboot.dto.PostResponse;
import com.project.springboot.repository.PostsRepository;
import com.project.springboot.service.PostService;
import com.project.springboot.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
public class PostController {

    private PostService postService;

    @Autowired
    public PostController(PostService postService,
                          PostsRepository postsRepository) {
        this.postService = postService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/v1/posts")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){
        return new ResponseEntity<>(postService.createPost(postDto),
                                    HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/posts")
    public PostResponse getAllPosts
            (
            @RequestParam(value = "pageNo",defaultValue = AppConstants.DEFAULT_PAGE_NO,required = false) int pageNum,
            @RequestParam(value = "pageSize",defaultValue = AppConstants.DEFAULT_PAGE_SIZE,required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = AppConstants.DEFAULT_SORT_BY,required = false)String sortBy,
            @RequestParam(value = "sortDir",defaultValue = AppConstants.DEFAULT_SORT_DIR,required = false)String sortDir
            )
    {

        return postService.getAllPost(pageNum,pageSize,sortBy,sortDir);
    }

    @GetMapping(value = "/api/v1/posts/{id}")
    public ResponseEntity<PostDto> getPostByIdV1(@PathVariable("id")long id){
        return ResponseEntity.ok(postService.getPostByid(id));
    }

    /*@GetMapping(value = "/api/posts/{id}",produces = "application/vnd.rahul.v2+json")
    public ResponseEntity<PostDtoV2> getPostByIdV2(@PathVariable("id")long id){
        PostDto postDto = postService.getPostByid(id);
        PostDtoV2 postDtoV2 = new PostDtoV2();
        postDtoV2.setId(postDto.getId());
        postDtoV2.setTitle(postDto.getTitle());
        postDtoV2.setDescription(postDto.getDescription());
        postDtoV2.setContent(postDto.getContent());
        List<String > tags = new ArrayList<>();
        tags.add("Java");
        tags.add("Spring");
        tags.add("AWS");
        postDtoV2.setTags(tags);
        return ResponseEntity.ok(postDtoV2);
    }*/

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto,@PathVariable(name = "id") long id){
     PostDto postResponse= postService.updatePost(postDto,id);
     return new ResponseEntity<>(postResponse,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/v1/posts/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") long id){
        postService.deletePostById(id);
        return new ResponseEntity<>("Post is deleted",HttpStatus.OK);
    }

    //get posts by category id
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("id") Long categoryId){
      List<PostDto> postDtos=  postService.getPostByCategory(categoryId);
      return ResponseEntity.ok(postDtos);
    }
}
