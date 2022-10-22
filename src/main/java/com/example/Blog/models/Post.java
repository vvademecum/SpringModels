package com.example.Blog.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Entity
public class Post {
    public Post(String title, String anons, String full_text, Fish fish) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.fish = fish;
    }
    public Post() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 10, max = 200, message = "Поле должно иметь размер от 10 до 200 символов")
    private String title;
    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 10, max = 200, message = "Поле должно иметь размер от 10 до 200 символов")
    private String anons;

    @NotBlank(message = "Поле не должно быть пустым")
    private String full_text;
    @ManyToOne
    @JoinColumn(name = "fish_id", referencedColumnName = "id")
    private Fish fish;
    private int views;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fish getFish() {return fish;}

    public void setFish(Fish fish) {this.fish = fish;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
