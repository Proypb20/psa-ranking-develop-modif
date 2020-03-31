package com.ar.pbpoints.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.ar.pbpoints.domain.Format} entity.
 */
public class FormatDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Float coeficient;

    private Integer playersQty;


    private Long tournamentId;

    private String tournamentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getCoeficient() {
        return coeficient;
    }

    public void setCoeficient(Float coeficient) {
        this.coeficient = coeficient;
    }

    public Integer getPlayersQty() {
        return playersQty;
    }

    public void setPlayersQty(Integer playersQty) {
        this.playersQty = playersQty;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FormatDTO formatDTO = (FormatDTO) o;
        if (formatDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), formatDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FormatDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", coeficient=" + getCoeficient() +
            ", playersQty=" + getPlayersQty() +
            ", tournament=" + getTournamentId() +
            ", tournament='" + getTournamentName() + "'" +
            "}";
    }
}
