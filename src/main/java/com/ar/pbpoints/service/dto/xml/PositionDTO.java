package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "POSITION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionDTO {

    @JacksonXmlProperty(localName = "NRO")
    @NotNull
    private Integer pos;
    @JacksonXmlProperty(localName = "TEAM")
    @NotNull
    private String teamName;
    @JacksonXmlProperty(localName = "POINTS")
    @NotNull
    private Float points;

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
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
            "pos=" + pos +
            ", teamName=" + teamName +
            ", points='" + points +
            '}';
    }
}
