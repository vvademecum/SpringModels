package com.example.Blog.repo;

import com.example.Blog.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findByTitleContains(String title);
    Iterable<Post> findByTitle(String title);
}
