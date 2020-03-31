package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.TournamentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tournament} and its DTO {@link TournamentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TournamentMapper extends EntityMapper<TournamentDTO, Tournament> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    TournamentDTO toDto(Tournament tournament);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "removeEvent", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    Tournament toEntity(TournamentDTO tournamentDTO);

    default Tournament fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tournament tournament = new Tournament();
        tournament.setId(id);
        return tournament;
    }
}
