package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JacksonXmlRootElement(localName = "fixture")
@JsonIgnoreProperties(ignoreUnknown = true)
public class FixtureDTO {

    @JacksonXmlProperty(localName = "category")
    @Valid
    @NotNull
    private CategoryDTO categoryDTO;

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    @Override
    public String toString() {
        return "FixtureDTO{" +
            "categoryDTO=" + categoryDTO +
            '}';
    }
}
