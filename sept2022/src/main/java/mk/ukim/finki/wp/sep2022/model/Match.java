package mk.ukim.finki.wp.sep2022.model;

import jakarta.persistence.*;

@Entity
public class Match {

    public Match() {
    }

    public Match(String name, String description, Double price, MatchType type, MatchLocation location) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.matchType = type;
        this.location = location;
        this.follows = 0;
    }
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private Double price;
    @Enumerated(EnumType.STRING)
    private MatchType matchType;
    @ManyToOne
    private MatchLocation location;

    private Integer follows = 0;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MatchType getType() {
        return matchType;
    }

    public void setType(MatchType type) {
        this.matchType = type;
    }

    public MatchLocation getLocation() {
        return location;
    }

    public void setLocation(MatchLocation location) {
        this.location = location;
    }

    public Integer getFollows() {
        return follows;
    }

    public void setFollows(Integer follows) {
        this.follows = follows;
    }
}
