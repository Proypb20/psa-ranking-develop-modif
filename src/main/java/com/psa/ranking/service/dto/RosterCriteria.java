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
 * Criteria class for the {@link com.psa.ranking.domain.Roster} entity. This class is used
 * in {@link com.psa.ranking.web.rest.RosterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /rosters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RosterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter active;

    private LongFilter categoryId;

    private LongFilter teamId;

    private LongFilter tournamentId;

    private LongFilter eventId;

    public RosterCriteria(){
    }

    public RosterCriteria(RosterCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.teamId = other.teamId == null ? null : other.teamId.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
    }

    @Override
    public RosterCriteria copy() {
        return new RosterCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getTeamId() {
        return teamId;
    }

    public void setTeamId(LongFilter teamId) {
        this.teamId = teamId;
    }

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RosterCriteria that = (RosterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(active, that.active) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(teamId, that.teamId) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        active,
        categoryId,
        teamId,
        tournamentId,
        eventId
        );
    }

    @Override
    public String toString() {
        return "RosterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (teamId != null ? "teamId=" + teamId + ", " : "") +
                (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
            "}";
    }

}
