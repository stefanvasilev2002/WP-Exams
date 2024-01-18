package mk.ukim.finki.wp.exam.example.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Product {

    public Product() {
    }

    public Product(String name, Double price, Integer quantity, List<Category> categories) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.categories = categories;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

    @ManyToMany
    private List<Category> categories;

    @ManyToOne
    private User creator;


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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
