package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.psa.ranking.domain.Roster} entity.
 */
@ApiModel(description = "Roster entity.\n@author Marcelo Miño")
public class RosterDTO implements Serializable {

    private Long id;

    private Boolean active;


    private Long categoryId;

    private Long teamId;

    private String teamName;

    private Long tournamentId;

    private String tournamentName;

    private Long eventId;

    private String eventName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RosterDTO rosterDTO = (RosterDTO) o;
        if (rosterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rosterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RosterDTO{" +
            "id=" + getId() +
            ", active='" + isActive() + "'" +
            ", category=" + getCategoryId() +
            ", team=" + getTeamId() +
            ", team='" + getTeamName() + "'" +
            ", tournament=" + getTournamentId() +
            ", tournament='" + getTournamentName() + "'" +
            ", event=" + getEventId() +
            ", event='" + getEventName() + "'" +
            "}";
    }
}
