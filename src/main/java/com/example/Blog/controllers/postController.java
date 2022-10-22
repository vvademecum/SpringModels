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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
                             @RequestParam(required = false) Boolean exactSearch,
                             HttpSession session, Model model) {

        Post post = postRepository.findById(id).get();
        List<Comment> commentList = commentRepository.findCommentByPost(post);

        if (text != null && text != "") {
            if (exactSearch != null && exactSearch == true)
                commentList = commentRepository.findCommentByPostAndText(post, text.trim());
            else
                commentList = commentRepository.findCommentByPostAndTextContains(post, text.trim());
        }

        model.addAttribute("comments", commentList);
        model.addAttribute("post", post);

        if (session.getAttribute("comment") != null && session.getAttribute("flash") != null) {
            model.addAttribute("comment", session.getAttribute("comment"));
            model.addAttribute("org.springframework.validation.BindingResult.comment", session.getAttribute("org.springframework.validation.BindingResult.comment"));
            session.removeAttribute("flash");
        } else {
            session.removeAttribute("comment");
            session.removeAttribute("org.springframework.validation.BindingResult.comment");
            model.addAttribute("comment", new Comment());
        }

        return "post";
    }

    @GetMapping("/addPost")
    public String toAddPostForm(Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        model.addAttribute("fishes", fishes);
        model.addAttribute("post", new Post());

        return "addPost";
    }

    @PostMapping("/post/editPostPage")
    public String toEditPostPage(@RequestParam long id, Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        model.addAttribute("fishes", fishes);

        Post post = postRepository.findById(id).get();
        model.addAttribute("post", post);

        return "editPost";
    }

    @PostMapping("/editPost")
    public String toPostAfterEdit(@ModelAttribute("post")
                                  @Valid Post post,
                                  BindingResult bindingResult,
                                  @RequestParam Long fish_id, Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        Fish fish = fishRepository.findById(fish_id).get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("fishes", fishes);
            model.addAttribute("fish", fish);
            model.addAttribute("post", post);

            return "editPost";
        }

        Post _post = postRepository.findById(post.getId()).get();


        _post.setTitle(post.getTitle());
        _post.setAnons(post.getAnons());
        _post.setFull_text(post.getFull_text());
        _post.setFish(fish);

        postRepository.save(_post);

        return "redirect:/selectedPost?id=" + String.valueOf(post.getId());
    }

    @PostMapping("/post/delete")
    public String deletePost(@RequestParam long id,
                             Model model) {

        Post post = postRepository.findById(id).get();
        postRepository.delete(post);

        return "redirect:/";
    }

    @PostMapping("/createPost")
    public String toMainAfterCreate(@ModelAttribute("post")
                                    @Valid Post post,
                                    BindingResult bindingResult,
                                    @RequestParam Long fish_id, Model model) {


        if (bindingResult.hasErrors()) {
            Iterable<Fish> fishes = fishRepository.findAll();
            model.addAttribute("fishes", fishes);
            model.addAttribute("post", post);
            return "addPost";
        }

        Fish fish = fishRepository.findById(fish_id).get();
        Post _post = new Post(post.getTitle(), post.getAnons(), post.getFull_text(), fish);
        postRepository.save(_post);

        return "redirect:/";
    }
}
