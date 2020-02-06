package com.psa.ranking.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A TeamPoint.
 */
@Entity
@Table(name = "team_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeamPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @ManyToOne(optional = false)
    @NotNull
    private Team team;

    @ManyToOne
    private Tournament tournament;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoints() {
        return points;
    }

    public TeamPoint points(Float points) {
        this.points = points;
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Team getTeam() {
        return team;
    }

    public TeamPoint team(Team team) {
        this.team = team;
        return this;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public TeamPoint tournament(Tournament tournament) {
        this.tournament = tournament;
        return this;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamPoint)) {
            return false;
        }
        return id != null && id.equals(((TeamPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TeamPoint{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            "}";
    }
}
