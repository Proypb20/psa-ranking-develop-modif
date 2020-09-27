package com.ar.pbpoints.service.dto.xml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.List;

@JacksonXmlRootElement(localName = "PBPOINTS")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResultDTO {

    @NotNull
    @JacksonXmlProperty(localName = "EVENT_ID")
    private Long event_id;

    @NotNull
    @JacksonXmlProperty(localName = "OWNER_ID")
    private Long owner_id;

    @JacksonXmlProperty(localName = "PASS")
    private String password;

    @JacksonXmlProperty(localName = "FIXTURE")
    @Valid
    @NotNull
    private FixtureDTO fixtureDTO;

    public Long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

    public Long getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Long owner_id) {
        this.owner_id = owner_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FixtureDTO getFixtureDTO() {
        return fixtureDTO;
    }

    public void setFixtureDTO(FixtureDTO fixtureDTO) {
        this.fixtureDTO = fixtureDTO;
    }

    @Override
    public String toString() {
        return "GameResultDTO{" +
            "event_id=" + event_id +
            ", owner_id=" + owner_id +
            ", password='" + password + '\'' +
            ", fixtureDTO=" + fixtureDTO +
            '}';
    }
}
