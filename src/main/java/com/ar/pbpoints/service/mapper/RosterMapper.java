package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.RosterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Roster} and its DTO {@link RosterDTO}.
 */
@Mapper(componentModel = "spring", uses = {TeamMapper.class, EventCategoryMapper.class})
public interface RosterMapper extends EntityMapper<RosterDTO, Roster> {

    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "team.name", target = "teamName")
    @Mapping(source = "eventCategory.id", target = "eventCategoryId")
    RosterDTO toDto(Roster roster);

    @Mapping(source = "teamId", target = "team")
    @Mapping(source = "eventCategoryId", target = "eventCategory")
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "removePlayer", ignore = true)
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
