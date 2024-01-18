package mk.ukim.finki.wp.eshop.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 4000)
    private String description;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
