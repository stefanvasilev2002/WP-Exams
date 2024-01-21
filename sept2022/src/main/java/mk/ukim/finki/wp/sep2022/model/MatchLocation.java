package mk.ukim.finki.wp.sep2022.model;

import jakarta.persistence.*;

@Entity
public class MatchLocation {

    public MatchLocation() {
    }

    public MatchLocation(String name) {
        this.name = name;
    }
    @Id
    @GeneratedValue
    private Long id;

    private String name;

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
}
