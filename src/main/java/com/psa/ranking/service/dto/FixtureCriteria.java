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
 * Criteria class for the {@link com.psa.ranking.domain.Fixture} entity. This class is used
 * in {@link com.psa.ranking.web.rest.FixtureResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /fixtures?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FixtureCriteria implements Serializable, Criteria {
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

    private StatusFilter status;

    private LongFilter eventCategoryId;

    public FixtureCriteria(){
    }

    public FixtureCriteria(FixtureCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.eventCategoryId = other.eventCategoryId == null ? null : other.eventCategoryId.copy();
    }

    @Override
    public FixtureCriteria copy() {
        return new FixtureCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public LongFilter getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(LongFilter eventCategoryId) {
        this.eventCategoryId = eventCategoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FixtureCriteria that = (FixtureCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(eventCategoryId, that.eventCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        eventCategoryId
        );
    }

    @Override
    public String toString() {
        return "FixtureCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (eventCategoryId != null ? "eventCategoryId=" + eventCategoryId + ", " : "") +
            "}";
    }

}
