package com.example.Blog.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@Entity
public class Post {
    public Post(String title, String anons, String full_text, List<Fish> postFishes, User user) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.postFishes = postFishes;
        this.user = user;
    }

    public Post() {
    }

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
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private int views;
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList;
    @ManyToMany
    @JoinTable(name = "post_fish", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "fish_id"))
    public List<Fish> postFishes;

    public List<Fish> getPostFishes() {
        return postFishes;
    }

    public void setPostFishes(List<Fish> postFishes) {
        this.postFishes = postFishes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
