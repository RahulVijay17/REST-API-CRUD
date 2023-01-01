package com.project.springboot.serviceImpl;

import com.project.springboot.dto.PostDto;
import com.project.springboot.dto.PostResponse;
import com.project.springboot.entity.Category;
import com.project.springboot.entity.Post;
import com.project.springboot.exception.ResourceNotFoundException;
import com.project.springboot.repository.CategoryRepository;
import com.project.springboot.repository.PostsRepository;
import com.project.springboot.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PostServiceImpl implements PostService {
    private PostsRepository postsRepository;

    private ModelMapper modelMapper;

    private CategoryRepository categoryRepository;

    @Autowired
    public PostServiceImpl(PostsRepository postsRepository, ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.postsRepository = postsRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category =categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException
                        ("Category","id",postDto.getCategoryId()));
                 //convert entity to dto
        Post post=mapToEntity(postDto);
        /*post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
*/      post.setCategory(category);
        Post newPost= postsRepository.save(post);

                //convert dto to entity
        PostDto postResponse=mapToDto(newPost);
        /*postResponse.setId(newPost.getId());
        postResponse.setTitle(newPost.getTitle());
        postResponse.setDescription(newPost.getDescription());
        postResponse.setContent(newPost.getContent());*/

        return postResponse;
    }

    @Override
    public PostResponse getAllPost(int pageNum,int pageSize,String sortBy,String sortDir) {

        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
            ? Sort.by(sortBy).ascending() :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNum,pageSize,sort);

        Page<Post> posts = postsRepository.findAll(pageable);

        List<Post> listOfPosts= posts.getContent();

        List<PostDto> content = listOfPosts.stream()
                .map(post->mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse= new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }
    @Override
    public PostDto getPostByid(long id) {
        Post post = postsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));

        Category category =categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException
                        ("Category","id",postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        Post updatedPost=postsRepository.save(post);
        return mapToDto(updatedPost);
    }

    @Override
    public void deletePostById(long id) {
        Post post = postsRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postsRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostByCategory(Long categoryId) {

        Category category =categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException
                        ("Category","id",categoryId));

        List<Post> posts = postsRepository.findByCategoryId(categoryId);

        return posts.stream().map((post)->mapToDto(post)).collect(Collectors.toList());
    }

    //entity to dto
    private PostDto mapToDto(Post post){
        PostDto postDto=modelMapper.map(post,PostDto.class);
        /*PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());*/
        return  postDto;
    }

    //dto to entity
    private Post mapToEntity(PostDto postDto){
        Post post=modelMapper.map(postDto,Post.class);
        /*Post post=new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());*/
        return post;
    }

}
