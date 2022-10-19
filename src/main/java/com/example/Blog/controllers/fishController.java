package com.example.Blog.controllers;

import com.example.Blog.models.Comment;
import com.example.Blog.models.Fish;
import com.example.Blog.models.Post;
import com.example.Blog.repo.FishRepository;
import com.example.Blog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class fishController {

    @Autowired
    private FishRepository fishRepository;

    @GetMapping("/addFish")
    public String toAddFishForm(Model model){

        return "addFish";
    }

    @GetMapping("/fishes")
    public String toFishes(@RequestParam(required = false) String nameFish,
                           @RequestParam(required = false) Boolean exactSearch, Model model) {

        Iterable<Fish> fish = new ArrayList<Fish>();

        if(nameFish != null && nameFish != "") {
            if(exactSearch != null && exactSearch == true)
                fish = fishRepository.findByName(nameFish);
            else
                fish = fishRepository.findByNameContains(nameFish);
        }
        else
            fish = fishRepository.findAll();

        model.addAttribute("fishes", fish);
        return "fishes";
    }

    @GetMapping("/selectedFish")
    public String getOneFish(@RequestParam(required = false) String text,
                             @RequestParam long id, Model model) {

        Fish fish = fishRepository.findById(id).get();
        model.addAttribute("fish", fish);

        return "fish";
    }

    @PostMapping("/fish/editFishPage")
    public String toEditPostPage(@RequestParam long id, Model model){

        Fish fish = fishRepository.findById(id).get();
        model.addAttribute("fish", fish);

        return "editFish";
    }

    @PostMapping("/editFish")
    public String toPostAfterEdit(@RequestParam long id,
                              @RequestParam String name,
                              @RequestParam double averageWeight,
                              @RequestParam int iq,
                              @RequestParam(required = false) Boolean redBook,
                              @RequestParam String color, Model model){

        Fish fish = fishRepository.findById(id).get();

        fish.setName(name);
        fish.setAverageWeight(averageWeight);
        fish.setIq(iq);
        fish.setRedBook(redBook == null ? false : redBook);
        fish.setColor(color);

        fishRepository.save(fish);

        return "redirect:/selectedFish?id="+String.valueOf(id);
    }

    @PostMapping("/fish/delete")
    public String deletePost(@RequestParam long id,
                             Model model){

        Fish fish = fishRepository.findById(id).get();
        fishRepository.delete(fish);

        return "redirect:/fishes";
    }

    @PostMapping("/createFish")
    public String toMainAfterCreateFish(@RequestParam String name,
                                    @RequestParam double averageWeight,
                                    @RequestParam int iq,
                                    @RequestParam(required = false) Boolean redBook,
                                    @RequestParam String color, Model model) {

        Fish fish = new Fish(name, averageWeight, iq, redBook == null ? false : redBook, color);
        fishRepository.save(fish);

        return "redirect:/addPost";
    }

}
