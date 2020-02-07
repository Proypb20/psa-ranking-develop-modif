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
 * Criteria class for the {@link com.psa.ranking.domain.EventCategory} entity. This class is used
 * in {@link com.psa.ranking.web.rest.EventCategoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-categories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCategoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter eventId;

    private LongFilter categoryId;

    private LongFilter formatId;

    public EventCategoryCriteria(){
    }

    public EventCategoryCriteria(EventCategoryCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.formatId = other.formatId == null ? null : other.formatId.copy();
    }

    @Override
    public EventCategoryCriteria copy() {
        return new EventCategoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getFormatId() {
        return formatId;
    }

    public void setFormatId(LongFilter formatId) {
        this.formatId = formatId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCategoryCriteria that = (EventCategoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(formatId, that.formatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        eventId,
        categoryId,
        formatId
        );
    }

    @Override
    public String toString() {
        return "EventCategoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (eventId != null ? "eventId=" + eventId + ", " : "") +
                (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
                (formatId != null ? "formatId=" + formatId + ", " : "") +
            "}";
    }

}
