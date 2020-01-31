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
 * Criteria class for the {@link com.psa.ranking.domain.Tournament} entity. This class is used
 * in {@link com.psa.ranking.web.rest.TournamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tournaments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TournamentCriteria implements Serializable, Criteria {
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

    private StringFilter name;

    private IntegerFilter closeInscrDays;

    private StatusFilter status;

    private BooleanFilter categorize;

    private LongFilter eventId;

    private LongFilter ownerId;

    public TournamentCriteria(){
    }

    public TournamentCriteria(TournamentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.closeInscrDays = other.closeInscrDays == null ? null : other.closeInscrDays.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.categorize = other.categorize == null ? null : other.categorize.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.ownerId = other.ownerId == null ? null : other.ownerId.copy();
    }

    @Override
    public TournamentCriteria copy() {
        return new TournamentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getCloseInscrDays() {
        return closeInscrDays;
    }

    public void setCloseInscrDays(IntegerFilter closeInscrDays) {
        this.closeInscrDays = closeInscrDays;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getCategorize() {
        return categorize;
    }

    public void setCategorize(BooleanFilter categorize) {
        this.categorize = categorize;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(LongFilter ownerId) {
        this.ownerId = ownerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TournamentCriteria that = (TournamentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(closeInscrDays, that.closeInscrDays) &&
            Objects.equals(status, that.status) &&
            Objects.equals(categorize, that.categorize) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        closeInscrDays,
        status,
        categorize,
        eventId,
        ownerId
        );
    }

    @Override
    public String toString() {
        return "TournamentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (closeInscrDays != null ? "closeInscrDays=" + closeInscrDays + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (categorize != null ? "categorize=" + categorize + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
                (ownerId != null ? "ownerId=" + ownerId + ", " : "") +
            "}";
    }

}
