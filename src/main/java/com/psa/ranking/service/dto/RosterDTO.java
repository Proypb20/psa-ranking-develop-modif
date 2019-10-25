package com.psa.ranking.service.dto;
import io.swagger.annotations.ApiModel;
import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.psa.ranking.domain.enumeration.ProfileUser;

/**
 * A DTO for the {@link com.psa.ranking.domain.Roster} entity.
 */
@ApiModel(description = "Roster entity.\n@author Marcelo Miño")
public class RosterDTO implements Serializable {

    private Long id;

    private Boolean active;

    private ProfileUser profile;

    private Instant createDate;

    private Instant updatedDate;


    private Long categoryId;

    private Set<PlayerDTO> players = new HashSet<>();

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

    public ProfileUser getProfile() {
        return profile;
    }

    public void setProfile(ProfileUser profile) {
        this.profile = profile;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Set<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(Set<PlayerDTO> players) {
        this.players = players;
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
            ", profile='" + getProfile() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", category=" + getCategoryId() +
            "}";
    }
}
