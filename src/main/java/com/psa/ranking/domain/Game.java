package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.psa.ranking.domain.enumeration.Status;

/**
 * A Game.
 */
@Entity
@Table(name = "game")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "points_a")
    private Integer pointsA;

    @Column(name = "points_b")
    private Integer pointsB;

    @Column(name = "split_deck_num")
    private Integer splitDeckNum;

    @Column(name = "time_left")
    private Integer timeLeft;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("games")
    private Fixture fixture;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("games")
    private Team teamA;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("games")
    private Team teamB;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("games")
    private EventCategory eventCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPointsA() {
        return pointsA;
    }

    public Game pointsA(Integer pointsA) {
        this.pointsA = pointsA;
        return this;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getPointsB() {
        return pointsB;
    }

    public Game pointsB(Integer pointsB) {
        this.pointsB = pointsB;
        return this;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getSplitDeckNum() {
        return splitDeckNum;
    }

    public Game splitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
        return this;
    }

    public void setSplitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public Game timeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
        return this;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Status getStatus() {
        return status;
    }

    public Game status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public Game fixture(Fixture fixture) {
        this.fixture = fixture;
        return this;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public Team getTeamA() {
        return teamA;
    }

    public Game teamA(Team team) {
        this.teamA = team;
        return this;
    }

    public void setTeamA(Team team) {
        this.teamA = team;
    }

    public Team getTeamB() {
        return teamB;
    }

    public Game teamB(Team team) {
        this.teamB = team;
        return this;
    }

    public void setTeamB(Team team) {
        this.teamB = team;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public Game eventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
        return this;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Game)) {
            return false;
        }
        return id != null && id.equals(((Game) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Game{" +
            "id=" + getId() +
            ", pointsA=" + getPointsA() +
            ", pointsB=" + getPointsB() +
            ", splitDeckNum=" + getSplitDeckNum() +
            ", timeLeft=" + getTimeLeft() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
