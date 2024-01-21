package mk.ukim.finki.wp.jan2022.g1.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Task {

    public Task() {
    }

    public Task(String title, String description, TaskCategory category, List<User> assignees, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.assignees = assignees;
        this.dueDate = dueDate;
    }
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate dueDate;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskCategory category;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> assignees;

    private Boolean done = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<User> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<User> assignees) {
        this.assignees = assignees;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
