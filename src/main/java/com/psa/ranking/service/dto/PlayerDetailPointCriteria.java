package com.psa.ranking.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.psa.ranking.domain.PlayerDetailPoint} entity. This class is used
 * in {@link com.psa.ranking.web.rest.PlayerDetailPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /player-detail-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerDetailPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter points;

    private LongFilter eventId;

    private LongFilter playerPointId;

    public PlayerDetailPointCriteria(){
    }

    public PlayerDetailPointCriteria(PlayerDetailPointCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.playerPointId = other.playerPointId == null ? null : other.playerPointId.copy();
    }

    @Override
    public PlayerDetailPointCriteria copy() {
        return new PlayerDetailPointCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getPoints() {
        return points;
    }

    public void setPoints(FloatFilter points) {
        this.points = points;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getPlayerPointId() {
        return playerPointId;
    }

    public void setPlayerPointId(LongFilter playerPointId) {
        this.playerPointId = playerPointId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayerDetailPointCriteria that = (PlayerDetailPointCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(playerPointId, that.playerPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        points,
        eventId,
        playerPointId
        );
    }

    @Override
    public String toString() {
        return "PlayerDetailPointCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
                (playerPointId != null ? "playerPointId=" + playerPointId + ", " : "") +
            "}";
    }

}
