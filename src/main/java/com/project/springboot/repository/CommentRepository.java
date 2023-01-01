package com.project.springboot.repository;

import com.project.springboot.entity.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment,Long> {

    List<Comment> findByPostId(long id);

}
