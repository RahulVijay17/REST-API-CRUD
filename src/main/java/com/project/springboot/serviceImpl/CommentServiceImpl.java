package com.project.springboot.serviceImpl;

import com.project.springboot.dto.CommentDto;
import com.project.springboot.entity.Comment;
import com.project.springboot.entity.Post;
import com.project.springboot.exception.BlogAPIException;
import com.project.springboot.exception.ResourceNotFoundException;
import com.project.springboot.repository.CommentRepository;
import com.project.springboot.repository.PostsRepository;
import com.project.springboot.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    private PostsRepository postsRepository;

    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,PostsRepository postsRepository,ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postsRepository=postsRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment=mapToEntity(commentDto);

        Post post =postsRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        comment.setPost(post);
       Comment newComment = commentRepository.save(comment);

       return mapToDto(newComment);
    }

    @Override
    public List<CommentDto> getCommentsByPostid(long postId) {
        List<Comment> comments=commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment))
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post =postsRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }
        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentRequest) {
        Post post =postsRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));

        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        Post post =postsRepository.findById(postId)
                .orElseThrow(()->new ResourceNotFoundException("Post","id",postId));
        Comment comment=commentRepository.findById(commentId)
                .orElseThrow(()->new ResourceNotFoundException("comment","id",commentId));
        if (!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belong to post");
        }

        commentRepository.delete(comment);
    }

    private CommentDto mapToDto(Comment comment){
        CommentDto commentDto = modelMapper.map(comment,CommentDto.class);
     /*   CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());*/
        return commentDto;
    }
    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto,Comment.class);
        /*Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody())*/;
        return comment;
    }
}
