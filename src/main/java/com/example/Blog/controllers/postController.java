package com.example.Blog.controllers;

import com.example.Blog.models.Comment;
import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.repo.CommentRepository;
import com.example.Blog.repo.FishRepository;
import com.example.Blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class postController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FishRepository fishRepository;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/selectedPost")
    public String getOnePost(@RequestParam(required = false) String text,
                             @RequestParam long id,
                             @RequestParam(required = false) Boolean exactSearch, Model model) {

        Post post = postRepository.findById(id).get();
        List<Comment> commentList = commentRepository.findCommentByPost(post);

        if(text != null && text != "") {
            if(exactSearch != null && exactSearch == true)
                commentList = commentRepository.findCommentByPostAndText(post, text.trim());
            else
                commentList = commentRepository.findCommentByPostAndTextContains(post, text.trim());
        }

        model.addAttribute("comments", commentList);
        model.addAttribute("post", post);

        return "post";
    }

    @GetMapping("/addPost")
    public String toAddPostForm(Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        model.addAttribute("fishes", fishes);

        return "addPost";
    }

    @PostMapping("/createPost")
    public String toMainAfterCreate(@RequestParam String title,
                                    @RequestParam String anons,
                                    @RequestParam String full_text,
                                    @RequestParam Long fish_id, Model model) {

        Fish fish = fishRepository.findById(fish_id).get();
        Post post = new Post(title, anons, full_text, fish);
        postRepository.save(post);

        return "redirect:/";
    }

}
