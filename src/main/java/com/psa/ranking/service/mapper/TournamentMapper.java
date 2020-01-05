package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.TournamentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tournament} and its DTO {@link TournamentDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserExtraMapper.class})
public interface TournamentMapper extends EntityMapper<TournamentDTO, Tournament> {

    @Mapping(source = "owner.id", target = "ownerId")
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
