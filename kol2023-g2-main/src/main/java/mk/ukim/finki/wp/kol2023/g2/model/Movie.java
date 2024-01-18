package mk.ukim.finki.wp.kol2023.g2.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Movie {

    public Movie() {
    }

    public Movie(String name, String description, Double rating, Genre genre, Director director) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.genre = genre;
        this.director = director;
        this.votes = 0;
    }
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Double rating;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    @ManyToOne
    private Director director;

    private Integer votes = 0;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setGenre(Genre position) {
        this.genre = position;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
