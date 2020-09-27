package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "POSITIONS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionDTO {

    @JacksonXmlProperty(localName = "NRO")
    @NotNull
    private Integer position;
    @JacksonXmlProperty(localName = "TEAM")
    @NotNull
    private String teamName;
    @JacksonXmlProperty(localName = "POINTS")
    @NotNull
    private Float points;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "PositionDTO{" +
            "position=" + position +
            ", teamName='" + teamName + '\'' +
            ", points=" + points +
            '}';
    }
}
