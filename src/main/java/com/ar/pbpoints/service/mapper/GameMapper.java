package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.GameDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = {TeamMapper.class, EventCategoryMapper.class})
public interface GameMapper extends EntityMapper<GameDTO, Game> {

    @Mapping(source = "teamA.id", target = "teamAId")
    @Mapping(source = "teamA.name", target = "teamAName")
    @Mapping(source = "teamB.id", target = "teamBId")
    @Mapping(source = "teamB.name", target = "teamBName")
    @Mapping(source = "eventCategory.id", target = "eventCategoryId")
    GameDTO toDto(Game game);

    @Mapping(source = "teamAId", target = "teamA")
    @Mapping(source = "teamBId", target = "teamB")
    @Mapping(source = "eventCategoryId", target = "eventCategory")
    Game toEntity(GameDTO gameDTO);

    default Game fromId(Long id) {
        if (id == null) {
            return null;
        }
        Game game = new Game();
        game.setId(id);
        return game;
    }
}
