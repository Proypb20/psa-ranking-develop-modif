package com.psa.ranking.service.dto;
import java.io.Serializable;
import java.util.Objects;
import com.psa.ranking.domain.enumeration.ProfileUser;

/**
 * A DTO for the {@link com.psa.ranking.domain.Player} entity.
 */
public class PlayerDTO implements Serializable {

    private Long id;

    private ProfileUser profile;

    private Boolean captainFlag;


    private Long userExtraId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileUser getProfile() {
        return profile;
    }

    public void setProfile(ProfileUser profile) {
        this.profile = profile;
    }

    public Boolean isCaptainFlag() {
        return captainFlag;
    }

    public void setCaptainFlag(Boolean captainFlag) {
        this.captainFlag = captainFlag;
    }

    public Long getUserExtraId() {
        return userExtraId;
    }

    public void setUserExtraId(Long userExtraId) {
        this.userExtraId = userExtraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PlayerDTO playerDTO = (PlayerDTO) o;
        if (playerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), playerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PlayerDTO{" +
            "id=" + getId() +
            ", profile='" + getProfile() + "'" +
            ", captainFlag='" + isCaptainFlag() + "'" +
            ", userExtra=" + getUserExtraId() +
            "}";
    }
}
