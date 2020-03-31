package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.PlayerPointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerPoint} and its DTO {@link PlayerPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {TournamentMapper.class, UserMapper.class, CategoryMapper.class})
public interface PlayerPointMapper extends EntityMapper<PlayerPointDTO, PlayerPoint> {

    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    PlayerPointDTO toDto(PlayerPoint playerPoint);

    @Mapping(source = "tournamentId", target = "tournament")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "categoryId", target = "category")
    PlayerPoint toEntity(PlayerPointDTO playerPointDTO);

    default PlayerPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlayerPoint playerPoint = new PlayerPoint();
        playerPoint.setId(id);
        return playerPoint;
    }
}
