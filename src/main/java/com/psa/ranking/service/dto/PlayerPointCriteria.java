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
 * Criteria class for the {@link com.psa.ranking.domain.PlayerPoint} entity. This class is used
 * in {@link com.psa.ranking.web.rest.PlayerPointResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /player-points?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlayerPointCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter points;

    private LongFilter tournamentId;

    private LongFilter userId;

    public PlayerPointCriteria(){
    }

    public PlayerPointCriteria(PlayerPointCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PlayerPointCriteria copy() {
        return new PlayerPointCriteria(this);
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

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlayerPointCriteria that = (PlayerPointCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        points,
        tournamentId,
        userId
        );
    }

    @Override
    public String toString() {
        return "PlayerPointCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
