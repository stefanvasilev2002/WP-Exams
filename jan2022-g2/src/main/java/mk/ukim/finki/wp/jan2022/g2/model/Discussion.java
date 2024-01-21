package mk.ukim.finki.wp.jan2022.g2.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Discussion {

    public Discussion() {
    }

    public Discussion(String title, String description, DiscussionTag discussionTag, List<User> participants, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.discussionTag = discussionTag;
        this.participants = participants;
        this.dueDate = dueDate;
    }
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dueDate;

    private String title;

    private String description;
    @Enumerated(EnumType.STRING)
    private DiscussionTag discussionTag;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> participants;

    private Boolean popular = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscussionTag getTag() {
        return discussionTag;
    }

    public void setTag(DiscussionTag discussionTag) {
        this.discussionTag = discussionTag;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Boolean getPopular() {
        return popular;
    }

    public void setPopular(Boolean popular) {
        this.popular = popular;
    }
}
