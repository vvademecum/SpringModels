package com.example.Blog.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Fish {
    public Fish(String name, double averageWeight, int iq, boolean redBook, String color) {
        this.name = name;
        this.averageWeight = averageWeight;
        this.iq = iq;
        this.redBook = redBook;
        this.color = color;
    }

    public Fish() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToMany(mappedBy = "fish", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Post> postList;
    private String name;
    private double averageWeight;
    private int iq;
    private boolean redBook;
    private String color;


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
