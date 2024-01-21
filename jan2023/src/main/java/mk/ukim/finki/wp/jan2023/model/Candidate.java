package mk.ukim.finki.wp.jan2023.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Candidate {

    public Candidate() {
    }

    public Candidate(String name, String bio, LocalDate dateOfBirth, Gender gender, Party party) {
        this.name = name;
        this.bio = bio;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.party = party;
        this.votes = 0;
    }
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String bio;

    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    private Party party;

    private Integer votes = 0;

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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
