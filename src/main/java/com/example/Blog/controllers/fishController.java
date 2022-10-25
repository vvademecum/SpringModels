package com.example.Blog.controllers;

import com.example.Blog.models.*;
import com.example.Blog.repo.FishRepository;
import com.example.Blog.repo.OnSaleRepository;
import com.example.Blog.repo.PostRepository;
import com.example.Blog.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class fishController {

    @Autowired
    private FishRepository fishRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OnSaleRepository onSaleRepository;

    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());

        return user;
    }

    @GetMapping("/addFish")
    public String toAddFishForm(Model model) {

        model.addAttribute("fish", new Fish());
        model.addAttribute("onSale", new OnSale());

        return "addFish";
    }

    @GetMapping("/fishes")
    public String toFishes(@RequestParam(required = false) String nameFish,
                           @RequestParam(required = false) Boolean exactSearch, Model model) {

        Iterable<Fish> fish = new ArrayList<Fish>();

        if (nameFish != null && nameFish != "") {
            if (exactSearch != null && exactSearch == true)
                fish = fishRepository.findByName(nameFish);
            else
                fish = fishRepository.findByNameContains(nameFish);
        } else
            fish = fishRepository.findAll();

        model.addAttribute("fishes", fish);

        postController.postFishesId.clear();
        return "fishes";
    }

    @GetMapping("/selectedFish")
    public String getOneFish(@RequestParam(required = false) String text,
                             @RequestParam long id, Model model) {

        Fish fish = fishRepository.findById(id).get();
        model.addAttribute("fish", fish);

        if (getAuthUser().getRoles().contains(Role.ADMIN))
            model.addAttribute("isAdmin", true);
        else
            model.addAttribute("isAdmin", false);

        if (fish.getUser().equals(getAuthUser()))
            model.addAttribute("entitled", true);
        else
            model.addAttribute("entitled", false);

        return "fish";
    }

    @PostMapping("/fish/editFishPage")
    public String toEditPostPage(@RequestParam long id, Model model) {

        Fish fish = fishRepository.findById(id).get();
        model.addAttribute("fish", fish);
        model.addAttribute("onSale", fish.getOnSale() != null ? fish.getOnSale() : new OnSale());

        return "editFish";
    }

    @PostMapping("/editFish")
    public String toPostAfterEdit(@ModelAttribute("fish")
                                  @Valid Fish fish,
                                  BindingResult bindingResult,
                                  OnSale onSale,
                                  @RequestParam(required = false) Boolean isOnSale,
                                  Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fish", fish);
            return "editFish";
        }

        Fish _fish = fishRepository.findById(fish.getId()).get();

        if (isOnSale == null ? false : isOnSale) {
            OnSale _onSale = _fish.getOnSale() != null ? _fish.getOnSale() : new OnSale();
            _onSale.setContactPhoneNumber(onSale.getContactPhoneNumber());
            _onSale.setPrice(onSale.getPrice());

            _fish.setOnSale(_onSale);
        } else {
            _fish.setOnSale(null);
        }

        _fish.setName(fish.getName());
        _fish.setAverageWeight(fish.getAverageWeight());
        _fish.setIq(fish.getIq());
        _fish.setRedBook(fish.getRedBook() != true ? false : fish.getRedBook());
        _fish.setColor(fish.getColor());
        _fish.setUser(getAuthUser());

        fishRepository.save(_fish);

        return "redirect:/selectedFish?id=" + String.valueOf(_fish.getId());
    }


    @PostMapping("/fish/delete")
    public String deletePost(@RequestParam long id,
                             Model model) {

        Fish fish = fishRepository.findById(id).get();
        fishRepository.delete(fish);

        return "redirect:/fishes";
    }

    @PostMapping("/createFish")
    public String toMainAfterCreateFish(@ModelAttribute("fish")
                                        @Valid Fish fish,
                                        BindingResult bindingResult,
                                        OnSale onSale,
                                        @RequestParam(required = false) Boolean isOnSale,
                                        Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("fish", fish);
            return "addFish";
        }



        Fish _fish = new Fish(fish.getName(),
                fish.getAverageWeight(),
                fish.getIq(),
                fish.getRedBook() != true ? false : fish.getRedBook(),
                fish.getColor(),
                getAuthUser());

        if (isOnSale == null ? false : isOnSale) {
            OnSale _onSale = new OnSale();
            _onSale.setContactPhoneNumber(onSale.getContactPhoneNumber());
            _onSale.setPrice(onSale.getPrice());

            _fish.setOnSale(_onSale);
        } else {
            _fish.setOnSale(null);
        }

        fishRepository.save(_fish);

        return "redirect:/addPost";
    }
}
