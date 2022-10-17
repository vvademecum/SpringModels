package com.example.Blog.controllers;

import com.example.Blog.models.Comment;
import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.repo.CommentRepository;
import com.example.Blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class commentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @PostMapping("/createComment")
    public String toMainAfterCreate(@RequestParam Long post_id,
                                    @RequestParam String text, Model model) {

        Post post = postRepository.findById(post_id).get();
        Comment comment = new Comment(text, new Date(), 0, 0, post);
        commentRepository.save(comment);

        return "redirect:/selectedPost?id="+String.valueOf(post_id);
    }

    //List<Comment> commentList = post.get().getCommentList();
}
