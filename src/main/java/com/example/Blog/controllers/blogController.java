package com.example.Blog.controllers;

import com.example.Blog.models.Post;
import com.example.Blog.models.Role;
import com.example.Blog.models.User;
import com.example.Blog.repo.PostRepository;
import com.example.Blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class blogController {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String getMainPage(@AuthenticationPrincipal User currentUser,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) Boolean exactSearch, Model model) {

        Iterable<Post> posts = new ArrayList<Post>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());
        model.addAttribute("userName", user.getUsername());
        if (user.getRoles().contains(Role.ADMIN))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);

        if (title != null && title != "") {
            if (exactSearch != null && exactSearch == true)
                posts = postRepository.findByTitle(title);
            else
                posts = postRepository.findByTitleContains(title);
        } else
            posts = postRepository.findAll();

        model.addAttribute("posts", posts);

        postController.postFishesId.clear();
        return "blogMain";
    }
}