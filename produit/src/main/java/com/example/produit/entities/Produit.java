package com.example.produit.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter

public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private Long userId;
    private Double price;

    public Produit(Long id, String name, String type, Long userId, Double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.userId = userId;
        this.price = price;
    }

    public Long getUserId() {
        return userId;
    }

    public Produit setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Produit setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Produit setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Produit setType(String type) {
        this.type = type;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public Produit() {
    }

    public Produit(Long id, String name, String type, Double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Produit setPrice(Double price) {
        this.price = price;
        return this;
    }
}
