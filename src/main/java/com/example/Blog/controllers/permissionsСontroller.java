package com.example.Blog.controllers;

import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.models.Role;
import com.example.Blog.models.User;
import com.example.Blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/rolesControl")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class permissionsСontroller {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getMainPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        Iterable<User> users = userRepository.findAllByUsernameNot(user.getUsername());
        model.addAttribute("users", users);

        return "rolesControl";
    }

    @GetMapping("/selectedUser")
    public String getOneUser(@RequestParam(required = false) String text,
                             @RequestParam long id,
                             Model model) {

        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);

        return "user";
    }


    @PostMapping("/editRoles")
    public String toEditPostPage(@ModelAttribute("post")
                                 @RequestParam long id,
                                 @RequestParam(required = false) Boolean commentModerator,
                                 @RequestParam(required = false) Boolean fishModerator,
                                 @RequestParam(required = false) Boolean admin,
                                 @RequestParam(required = false) Boolean postModerator,
                                 Model model) {

        User user = userRepository.findById(id).get();

        if(commentModerator == null ? false : commentModerator)
            user.getRoles().add(Role.COMMENT_MODERATOR);
        else
            user.getRoles().remove(Role.COMMENT_MODERATOR);

        if(fishModerator == null ? false : fishModerator)
            user.getRoles().add(Role.FISH_MODERATOR);
        else
            user.getRoles().remove(Role.FISH_MODERATOR);

        if(admin == null ? false : admin)
            user.getRoles().add(Role.ADMIN);
        else
            user.getRoles().remove(Role.ADMIN);

        if(postModerator == null ? false : postModerator)
            user.getRoles().add(Role.POST_MODERATOR);
        else
            user.getRoles().remove(Role.POST_MODERATOR);

        userRepository.save(user);

        return "redirect:/rolesControl";
    }
}
