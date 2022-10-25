package com.example.Blog.controllers;

import com.example.Blog.models.*;
import com.example.Blog.repo.CommentRepository;
import com.example.Blog.repo.FishRepository;
import com.example.Blog.repo.PostRepository;
import com.example.Blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
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
    @Autowired
    private UserRepository userRepository;

    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        return user;
    }

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
            model.addAttribute("org.springframework.validation.BindingResult.comment",
                    session.getAttribute("org.springframework.validation.BindingResult.comment"));
            session.removeAttribute("flash");
        } else {
            session.removeAttribute("comment");
            session.removeAttribute("org.springframework.validation.BindingResult.comment");
            model.addAttribute("comment", new Comment());
        }

        if (post.getUser().equals(getAuthUser()))
            model.addAttribute("entitled", true);
        else
            model.addAttribute("entitled", false);

        if (getAuthUser().getRoles().contains(Role.ADMIN))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);

        model.addAttribute("authUserId", getAuthUser().getId());
        return "post";
    }

    @GetMapping("/addPost")
    public String toAddPostForm(Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();

        model.addAttribute("fishes", fishes);
        model.addAttribute("post", new Post());

        model.addAttribute("user_id", getAuthUser().getId());

        postFishesId.clear();
        return "addPost";
    }

    @PostMapping("/post/editPostPage")
    public String toEditPostPage(@ModelAttribute("post")
                                 @RequestParam long id,
                                 Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        model.addAttribute("fishes", fishes);

        Post post = postRepository.findById(id).get();
        model.addAttribute("post", post);

        model.addAttribute("postFishes", post.getPostFishes());

        return "editPost";
    }

    @GetMapping("/addFishToListEdit")
    public String addFishToPostEdit(@ModelAttribute("post")
                                Post post,
                                @RequestParam Long fish_id, Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        model.addAttribute("fishes", fishes);

        model.addAttribute("post", post);

        Fish fish = fishRepository.findById(fish_id).get();
        if (!post.getPostFishes().contains(fish))
            post.getPostFishes().add(fish);
        model.addAttribute("postFishes", post.getPostFishes());


        return "editPost";
    }

    @GetMapping("/removeFishFromList")
    public String removeFishFromPost(@ModelAttribute("post")
                                    Post post,
                                    @RequestParam Long fish_id, Model model) {

        Iterable<Fish> fishes = fishRepository.findAll();
        model.addAttribute("fishes", fishes);

        model.addAttribute("post", post);

        Fish fish = fishRepository.findById(fish_id).get();
        if (post.getPostFishes().contains(fish)) {
            post.getPostFishes().remove(fish);
            //postRepository.save(post);
        }
        model.addAttribute("postFishes", post.getPostFishes());

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
//            model.addAttribute("fish", fish);
            model.addAttribute("post", post);
            model.addAttribute("postFishes", post.getPostFishes());

            return "editPost";
        }

        Post _post = postRepository.findById(post.getId()).get();
        _post.setTitle(post.getTitle());
        _post.setAnons(post.getAnons());
        _post.setFull_text(post.getFull_text());
        if(post.getPostFishes().size() == 0) {
            _post.getPostFishes().clear();
            _post.getPostFishes().add(fish);
        }
        else
            _post.setPostFishes(post.getPostFishes());
        _post.setUser(getAuthUser());
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

    static List<Long> postFishesId = new ArrayList<>();

    @GetMapping("/addFishToList")
    public String addFishToPost(@ModelAttribute("post")
                                Post post,
                                @RequestParam Long fish_id, Model model) {

        model.addAttribute("post", post);
        model.addAttribute("user_id", getAuthUser().getId());

        if (fish_id != null) {
            postFishesId.add(fish_id);
            model.addAttribute("postFishes", fishRepository.findByIdIn(postFishesId));
        }
        Iterable<Fish> fishes = fishRepository.findByIdNotIn(postFishesId);

        model.addAttribute("fishes", fishes);
        return "addPost";
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

            model.addAttribute("user_id", getAuthUser().getId());
            return "addPost";
        }

        if (postFishesId.size() == 0)
            postFishesId.add(fish_id);

        Post _post = new Post(post.getTitle(), post.getAnons(), post.getFull_text(), fishRepository.findByIdIn(postFishesId), getAuthUser());
        postRepository.save(_post);

        return "redirect:/";
    }
}
