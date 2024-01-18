package mk.ukim.finki.wp.kol2023.g1.model;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Player {

    public Player() {
    }

    public Player(String name, String bio, Double pointsPerGame, PlayerPosition position, Team team) {
        this.name = name;
        this.bio = bio;
        this.pointsPerGame = pointsPerGame;
        this.position = position;
        this.team = team;
        this.votes = 0;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String bio;

    private Double pointsPerGame;
    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    @ManyToOne(fetch = FetchType.EAGER)
    private Team team;

    private Integer votes = 0;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPointsPerGame(Double pointsPerGame) {
        this.pointsPerGame = pointsPerGame;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
