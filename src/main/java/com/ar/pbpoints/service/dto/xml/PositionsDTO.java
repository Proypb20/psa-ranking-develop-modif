package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@JacksonXmlRootElement(localName = "POSITIONS")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PositionsDTO {

    @JacksonXmlProperty(localName = "POSITION")
    @Valid
    @NotNull
    private List<PositionDTO> positions;

    public List<PositionDTO> getPositions() {
        return positions;
    }

    public void setGames(List<PositionDTO> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "PositionsDTO{" +
            "positions=" + positions +
            '}';
    }
}
