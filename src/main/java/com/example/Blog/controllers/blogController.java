package com.example.Blog.controllers;

import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.repo.FishRepository;
import com.example.Blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class blogController  {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FishRepository fishRepository;

    @GetMapping("/")
    public String auth(@RequestParam(required = false) String title,
                       @RequestParam(required = false) Boolean exactSearch, Model model) {

        Iterable<Post> posts = new ArrayList<Post>();

        if(title != null && title != "") {
            if(exactSearch != null && exactSearch == true)
                posts = postRepository.findByTitle(title);
            else
                posts = postRepository.findByTitleContains(title);
        }
        else
            posts = postRepository.findAll();

        model.addAttribute("posts", posts);
        return "blogMain";
    }

}