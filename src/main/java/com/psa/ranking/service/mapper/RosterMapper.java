package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.RosterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Roster} and its DTO {@link RosterDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, TeamMapper.class, TournamentMapper.class, EventMapper.class})
public interface RosterMapper extends EntityMapper<RosterDTO, Roster> {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "team.name", target = "teamName")
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    RosterDTO toDto(Roster roster);

    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "teamId", target = "team")
    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "eventId", target = "event")
    Roster toEntity(RosterDTO rosterDTO);

    default Roster fromId(Long id) {
        if (id == null) {
            return null;
        }
        Roster roster = new Roster();
        roster.setId(id);
        return roster;
    }
}
