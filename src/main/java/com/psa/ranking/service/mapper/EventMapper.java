package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = {TournamentMapper.class, CategoryMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event> {

    @Mapping(source = "tournament.id", target = "tournamentId")
    EventDTO toDto(Event event);

    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(target = "removeCategory", ignore = true)
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
