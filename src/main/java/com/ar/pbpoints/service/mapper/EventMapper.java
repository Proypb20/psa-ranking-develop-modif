package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = {TournamentMapper.class, CityMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event> {

    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "city.id", target = "cityId")
    EventDTO toDto(Event event);

    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "cityId", target = "city")
    Event toEntity(EventDTO eventDTO);

    default Event fromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
