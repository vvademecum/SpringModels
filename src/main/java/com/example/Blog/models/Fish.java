package com.example.Blog.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class Fish {
    public Fish(String name, double averageWeight, int iq, boolean redBook, String color, User user) {
        this.name = name;
        this.averageWeight = averageWeight;
        this.iq = iq;
        this.redBook = redBook;
        this.color = color;
        this.user = user;
    }

    public Fish() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 1, max = 100, message = "Поле должно иметь размер от 1 до 100 символов")
    private String name;
    @NotNull
    @Positive(message = "Значение должно быть положительным")
    private double averageWeight;
    @NotNull
    @Max(value = 200, message = "Значение должно быть не более 200")
    @PositiveOrZero(message = "Значение должно быть положительным или равно нулю")
    private int iq;
    @NotNull(message = "значение не должно быть null")
    private boolean redBook;
    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 1, max = 50, message = "Поле должно иметь размер от 1 до 50 символов")
    private String color;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(name = "post_fish",
            joinColumns = @JoinColumn(name = "fish_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    public List<Post> postFishes;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "onSale_id")
    private OnSale onSale;

    public OnSale getOnSale() {
        return onSale;
    }

    public void setOnSale(OnSale onSale) {
        this.onSale = onSale;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(double averageWeight) {
        this.averageWeight = averageWeight;
    }

    public int getIq() {
        return iq;
    }

    public void setIq(int iq) {
        this.iq = iq;
    }

    public boolean getRedBook() {
        return redBook;
    }

    public void setRedBook(boolean redBook) {
        this.redBook = redBook;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
