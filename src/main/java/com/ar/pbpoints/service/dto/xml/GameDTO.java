package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "category")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDTO {

    @NotNull
    private Long id;
    @JacksonXmlProperty(localName = "sd_id")
    @NotNull
    private Integer splitDeckNum;
    @JacksonXmlProperty(localName = "clasif")
    @NotNull
    private Integer clasification;
    @JacksonXmlProperty(localName = "team_a")
    @NotBlank
    private String teamA;
    @JacksonXmlProperty(localName = "points_a")
    @NotNull
    private Integer pointsA;
    @JacksonXmlProperty(localName = "overtime_a")
    @NotNull
    private Float overtimeA;
    @JacksonXmlProperty(localName = "uvu_a")
    @NotNull
    private Integer uvuA;
    @JacksonXmlProperty(localName = "team_b")
    @NotBlank
    private String teamB;
    @JacksonXmlProperty(localName = "points_b")
    private Integer pointsB;
    @JacksonXmlProperty(localName = "overtime_b")
    private Float overtimeB;
    @JacksonXmlProperty(localName = "uvu_b")
    @NotNull
    private Integer uvuB;
    @JacksonXmlProperty(localName = "time_left")
    @NotNull
    private Integer timeLeft;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSplitDeckNum() {
        return splitDeckNum;
    }

    public void setSplitDeckNum(Integer splitDeckNum) {
        this.splitDeckNum = splitDeckNum;
    }

    public Integer getClasification() {
        return clasification;
    }

    public void setClasification(Integer clasification) {
        this.clasification = clasification;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public Integer getPointsA() {
        return pointsA;
    }

    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }

    public Float getOvertimeA() {
        return overtimeA;
    }

    public void setOvertimeA(Float overtimeA) {
        this.overtimeA = overtimeA;
    }

    public Integer getUvuA() {
        return uvuA;
    }

    public void setUvuA(Integer uvuA) {
        this.uvuA = uvuA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public Integer getPointsB() {
        return pointsB;
    }

    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    public Float getOvertimeB() {
        return overtimeB;
    }

    public void setOvertimeB(Float overtimeB) {
        this.overtimeB = overtimeB;
    }

    public Integer getUvuB() {
        return uvuB;
    }

    public void setUvuB(Integer uvuB) {
        this.uvuB = uvuB;
    }

    public Integer getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }

    @Override
    public String toString() {
        return "GameDTO{" +
            "id=" + id +
            ", splitDeckNum=" + splitDeckNum +
            ", clasification=" + clasification +
            ", teamA='" + teamA + '\'' +
            ", pointsA=" + pointsA +
            ", overtimeA=" + overtimeA +
            ", uvuA=" + uvuA +
            ", teamB='" + teamB + '\'' +
            ", pointsB=" + pointsB +
            ", overtimeB=" + overtimeB +
            ", uvuB=" + uvuB +
            ", timeLeft=" + timeLeft +
            '}';
    }
}