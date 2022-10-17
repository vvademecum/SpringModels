package com.example.Blog.repo;

import com.example.Blog.models.Comment;
import com.example.Blog.models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findCommentByPost(Post post);
    List<Comment> findCommentByPostAndText(Post post, String text);
    List<Comment> findCommentByPostAndTextContains(Post post, String text);
}
