package com.psa.ranking.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.psa.ranking.domain.enumeration.Status;

/**
 * A DTO for the {@link com.psa.ranking.domain.Fixture} entity.
 */
public class FixtureDTO implements Serializable {

    private Long id;

    @NotNull
    private Status status;


    private Long eventId;

    private String eventName;

    private Long categoryId;

    private String categoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FixtureDTO fixtureDTO = (FixtureDTO) o;
        if (fixtureDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fixtureDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FixtureDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", event=" + getEventId() +
            ", event='" + getEventName() + "'" +
            ", category=" + getCategoryId() +
            ", category='" + getCategoryName() + "'" +
            "}";
    }
}
