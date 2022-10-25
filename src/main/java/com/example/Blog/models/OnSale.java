package com.example.Blog.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
public class OnSale {

    public OnSale(double price, String contactPhoneNumber, Fish fish) {
        this.price = price;
        this.contactPhoneNumber = contactPhoneNumber;
        this.fish = fish;
    }

    public OnSale(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @NotNull
//    @Positive(message = "Значение должно быть положительным")
    private double price;

//    @NotBlank(message = "Поле не должно быть пустым")
    private String contactPhoneNumber;

    @OneToOne(optional = true, mappedBy = "onSale")
    private Fish fish;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public Fish getFish() {
        return fish;
    }

    public void setFish(Fish fish) {
        this.fish = fish;
    }
}
