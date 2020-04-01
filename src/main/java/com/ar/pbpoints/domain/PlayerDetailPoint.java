package com.ar.pbpoints.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A PlayerDetailPoint.
 */
@Entity
@Table(name = "player_detail_point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlayerDetailPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "points", nullable = false)
    private Float points;

    @ManyToOne(optional = false)
    @NotNull
    private Event event;

    @ManyToOne(optional = false)
    @NotNull
    private PlayerPoint playerPoint;

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

    public PlayerDetailPoint points(Float points) {
        this.points = points;
        return this;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    public Event getEvent() {
        return event;
    }

    public PlayerDetailPoint event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public PlayerPoint getPlayerPoint() {
        return playerPoint;
    }

    public PlayerDetailPoint playerPoint(PlayerPoint playerPoint) {
        this.playerPoint = playerPoint;
        return this;
    }

    public void setPlayerPoint(PlayerPoint playerPoint) {
        this.playerPoint = playerPoint;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerDetailPoint)) {
            return false;
        }
        return id != null && id.equals(((PlayerDetailPoint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PlayerDetailPoint{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            "}";
    }
}
