package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@JacksonXmlRootElement(localName = "category")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDTO {

    @NotBlank
    private String name;
    @Valid
    @NotNull
    private List<GameDTO> games;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GameDTO> getGames() {
        return games;
    }

    public void setGames(List<GameDTO> games) {
        this.games = games;
    }

    @Override
    public String toString() {
        return "CategoryDTO{" +
            "name='" + name + '\'' +
            ", games=" + games +
            '}';
    }
}
