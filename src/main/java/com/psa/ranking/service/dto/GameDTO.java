package com.psa.ranking.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.psa.ranking.domain.enumeration.Status;

/**
 * A DTO for the {@link com.psa.ranking.domain.Game} entity.
 */
public class GameDTO implements Serializable {

    private Long id;

    private Integer pointsA;

    private Integer pointsB;

    private Integer splitDeckNum;

    private Integer timeLeft;

    @NotNull
    private Status status;


    private Long teamAId;

    private String teamAName;

    private Long teamBId;

    private String teamBName;

    private Long eventCategoryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPointsA() {
        return pointsA;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Integer getPointsB() {
        return pointsB;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Integer getSplitDeckNum() {
        return splitDeckNum;
    }

    public void setSplitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(Long teamId) {
        this.teamAId = teamId;
    }

    public String getTeamAName() {
        return teamAName;
    }

    public void setTeamAName(String teamName) {
        this.teamAName = teamName;
    }

    public Long getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(Long teamId) {
        this.teamBId = teamId;
    }

    public String getTeamBName() {
        return teamBName;
    }

    public void setTeamBName(String teamName) {
        this.teamBName = teamName;
    }

    public Long getEventCategoryId() {
        return eventCategoryId;
    }

    public void setEventCategoryId(Long eventCategoryId) {
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

        GameDTO gameDTO = (GameDTO) o;
        if (gameDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), gameDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + getId() +
            ", pointsA=" + getPointsA() +
            ", pointsB=" + getPointsB() +
            ", splitDeckNum=" + getSplitDeckNum() +
            ", timeLeft=" + getTimeLeft() +
            ", status='" + getStatus() + "'" +
            ", teamA=" + getTeamAId() +
            ", teamA='" + getTeamAName() + "'" +
            ", teamB=" + getTeamBId() +
            ", teamB='" + getTeamBName() + "'" +
            ", eventCategory=" + getEventCategoryId() +
            "}";
    }
}
