package mk.ukim.finki.wp.september2021.model;


import javax.persistence.*;

@Entity
public class News {

    public News() {
    }

    public News(String name, String description, Double price, NewsType type, NewsCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.category = category;
        this.likes = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    @Enumerated(EnumType.STRING)
    private NewsType type;

    @ManyToOne
    private NewsCategory category;

    private Integer likes = 0;

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

    public NewsType getType() {
        return type;
    }

    public void setType(NewsType type) {
        this.type = type;
    }

    public NewsCategory getCategory() {
        return category;
    }

    public void setCategory(NewsCategory location) {
        this.category = location;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer follows) {
        this.likes = follows;
    }
}
