package com.psa.ranking.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psa.ranking.domain.EventCategory} entity.
 */
public class EventCategoryDTO implements Serializable {

    private Long id;


    private Long eventId;

    private String eventName;

    private Long categoryId;

    private String categoryName;

    private Long formatId;

    private String formatName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getFormatId() {
        return formatId;
    }

    public void setFormatId(Long formatId) {
        this.formatId = formatId;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EventCategoryDTO eventCategoryDTO = (EventCategoryDTO) o;
        if (eventCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), eventCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EventCategoryDTO{" +
            "id=" + getId() +
            ", event=" + getEventId() +
            ", event='" + getEventName() + "'" +
            ", category=" + getCategoryId() +
            ", category='" + getCategoryName() + "'" +
            ", format=" + getFormatId() +
            ", format='" + getFormatName() + "'" +
            "}";
    }
}
