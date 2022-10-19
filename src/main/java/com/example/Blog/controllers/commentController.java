package com.example.Blog.controllers;

import com.example.Blog.models.Comment;
import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.repo.CommentRepository;
import com.example.Blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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

    @PostMapping("/comment/editCommentPage")
    public String toEditCommentPage(@RequestParam long id_comment,
                                    Model model) {

        Comment comment = commentRepository.findById(id_comment).get();
        model.addAttribute("comment", comment);

        return "editComment";
    }

    @PostMapping("/editComment")
    public String toPostAfterCommentEdit(@RequestParam long id,
                                         @RequestParam String text, Model model) {

        Comment comment = commentRepository.findById(id).get();
        comment.setText(text);
        commentRepository.save(comment);

        return "redirect:/selectedPost?id=" + comment.getPost().getId();
    }

    @PostMapping("comment/delete")
    public String commentDelete(@RequestParam long post_id,
                                @RequestParam long comment_id,
                                Model model) {

        Comment comment = commentRepository.findById(comment_id).get();
        commentRepository.delete(comment);
        return "redirect:/selectedPost?id="+post_id;
    }
}
