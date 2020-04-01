package com.ar.pbpoints.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A TeamDetailPoint.
 */
@Entity
@Table(name = "team_detail_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TeamDetailPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @ManyToOne(optional = false)
    @NotNull
    private TeamPoint teamPoint;

    @ManyToOne(optional = false)
    @NotNull
    private Event event;

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

    public TeamDetailPoint points(Float points) {
        this.points = points;
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public TeamPoint getTeamPoint() {
        return teamPoint;
    }

    public TeamDetailPoint teamPoint(TeamPoint teamPoint) {
        this.teamPoint = teamPoint;
        return this;
    }

    public void setTeamPoint(TeamPoint teamPoint) {
        this.teamPoint = teamPoint;
    }

    public Event getEvent() {
        return event;
    }

    public TeamDetailPoint event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeamDetailPoint)) {
            return false;
        }
        return id != null && id.equals(((TeamDetailPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TeamDetailPoint{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            "}";
    }
}
