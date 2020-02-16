package com.psa.ranking.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.psa.ranking.domain.enumeration.Status;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.psa.ranking.domain.Game} entity. This class is used
 * in {@link com.psa.ranking.web.rest.GameResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /games?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GameCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {
        }

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter pointsA;

    private IntegerFilter pointsB;

    private IntegerFilter splitDeckNum;

    private IntegerFilter timeLeft;

    private StatusFilter status;

    private LongFilter fixtureId;

    private LongFilter teamAId;

    private LongFilter teamBId;

    public GameCriteria(){
    }

    public GameCriteria(GameCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.pointsA = other.pointsA == null ? null : other.pointsA.copy();
        this.pointsB = other.pointsB == null ? null : other.pointsB.copy();
        this.splitDeckNum = other.splitDeckNum == null ? null : other.splitDeckNum.copy();
        this.timeLeft = other.timeLeft == null ? null : other.timeLeft.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.fixtureId = other.fixtureId == null ? null : other.fixtureId.copy();
        this.teamAId = other.teamAId == null ? null : other.teamAId.copy();
        this.teamBId = other.teamBId == null ? null : other.teamBId.copy();
    }

    @Override
    public GameCriteria copy() {
        return new GameCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPointsA() {
        return pointsA;
    }

    public void setPointsA(IntegerFilter pointsA) {
        this.pointsA = pointsA;
    }

    public IntegerFilter getPointsB() {
        return pointsB;
    }

    public void setPointsB(IntegerFilter pointsB) {
        this.pointsB = pointsB;
    }

    public IntegerFilter getSplitDeckNum() {
        return splitDeckNum;
    }

    public void setSplitDeckNum(IntegerFilter splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public IntegerFilter getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(IntegerFilter timeLeft) {
        this.timeLeft = timeLeft;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getFixtureId() {
        return fixtureId;
    }

    public void setFixtureId(LongFilter fixtureId) {
        this.fixtureId = fixtureId;
    }

    public LongFilter getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(LongFilter teamAId) {
        this.teamAId = teamAId;
    }

    public LongFilter getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(LongFilter teamBId) {
        this.teamBId = teamBId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GameCriteria that = (GameCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(pointsA, that.pointsA) &&
            Objects.equals(pointsB, that.pointsB) &&
            Objects.equals(splitDeckNum, that.splitDeckNum) &&
            Objects.equals(timeLeft, that.timeLeft) &&
            Objects.equals(status, that.status) &&
            Objects.equals(fixtureId, that.fixtureId) &&
            Objects.equals(teamAId, that.teamAId) &&
            Objects.equals(teamBId, that.teamBId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        pointsA,
        pointsB,
        splitDeckNum,
        timeLeft,
        status,
        fixtureId,
        teamAId,
        teamBId
        );
    }

    @Override
    public String toString() {
        return "GameCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (pointsA != null ? "pointsA=" + pointsA + ", " : "") +
                (pointsB != null ? "pointsB=" + pointsB + ", " : "") +
                (splitDeckNum != null ? "splitDeckNum=" + splitDeckNum + ", " : "") +
                (timeLeft != null ? "timeLeft=" + timeLeft + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (fixtureId != null ? "fixtureId=" + fixtureId + ", " : "") +
                (teamAId != null ? "teamAId=" + teamAId + ", " : "") +
                (teamBId != null ? "teamBId=" + teamBId + ", " : "") +
            "}";
    }

}
