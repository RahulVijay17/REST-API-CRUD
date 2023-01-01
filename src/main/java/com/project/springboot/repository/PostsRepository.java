package com.project.springboot.repository;

import com.project.springboot.entity.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends CrudRepository<Post,Long>, PagingAndSortingRepository<Post,Long> {

    List<Post> findByCategoryId(Long categoryId);
}
