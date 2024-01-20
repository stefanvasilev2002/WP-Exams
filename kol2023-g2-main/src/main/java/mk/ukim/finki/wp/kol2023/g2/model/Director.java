package mk.ukim.finki.wp.kol2023.g2.model;

import lombok.Getter;

import jakarta.persistence.*;

@Getter
@Entity
public class Director {

    public Director() {
    }

    public Director(String name) {
        this.name = name;
    }
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
