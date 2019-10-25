package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.PlayerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserExtraMapper.class})
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {

    @Mapping(source = "userExtra.id", target = "userExtraId")
    PlayerDTO toDto(Player player);

    @Mapping(source = "userExtraId", target = "userExtra")
    @Mapping(target = "rosters", ignore = true)
    @Mapping(target = "removeRoster", ignore = true)
    Player toEntity(PlayerDTO playerDTO);

    default Player fromId(Long id) {
        if (id == null) {
            return null;
        }
        Player player = new Player();
        player.setId(id);
        return player;
    }
}
