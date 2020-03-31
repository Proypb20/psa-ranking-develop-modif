package com.ar.pbpoints.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.ar.pbpoints.domain.enumeration.Status;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.ar.pbpoints.domain.Event} entity. This class is used
 * in {@link com.ar.pbpoints.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {
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

    private LocalDateFilter fromDate;

    private LocalDateFilter endDate;

    private LocalDateFilter endInscriptionDate;

    private StatusFilter status;

    private InstantFilter createDate;

    private InstantFilter updatedDate;

    private LongFilter tournamentId;

    private LongFilter cityId;

    public EventCriteria(){
    }

    public EventCriteria(EventCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.fromDate = other.fromDate == null ? null : other.fromDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.endInscriptionDate = other.endInscriptionDate == null ? null : other.endInscriptionDate.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.updatedDate = other.updatedDate == null ? null : other.updatedDate.copy();
        this.tournamentId = other.tournamentId == null ? null : other.tournamentId.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
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

    public LocalDateFilter getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateFilter fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public LocalDateFilter getEndInscriptionDate() {
        return endInscriptionDate;
    }

    public void setEndInscriptionDate(LocalDateFilter endInscriptionDate) {
        this.endInscriptionDate = endInscriptionDate;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public InstantFilter getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(InstantFilter updatedDate) {
        this.updatedDate = updatedDate;
    }

    public LongFilter getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(LongFilter tournamentId) {
        this.tournamentId = tournamentId;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fromDate, that.fromDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(endInscriptionDate, that.endInscriptionDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(updatedDate, that.updatedDate) &&
            Objects.equals(tournamentId, that.tournamentId) &&
            Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        fromDate,
        endDate,
        endInscriptionDate,
        status,
        createDate,
        updatedDate,
        tournamentId,
        cityId
        );
    }

    @Override
    public String toString() {
        return "EventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (fromDate != null ? "fromDate=" + fromDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (endInscriptionDate != null ? "endInscriptionDate=" + endInscriptionDate + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (updatedDate != null ? "updatedDate=" + updatedDate + ", " : "") +
                (tournamentId != null ? "tournamentId=" + tournamentId + ", " : "") +
                (cityId != null ? "cityId=" + cityId + ", " : "") +
            "}";
    }

}
