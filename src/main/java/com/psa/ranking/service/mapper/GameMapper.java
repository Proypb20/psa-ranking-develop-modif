package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.GameDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Game} and its DTO {@link GameDTO}.
 */
@Mapper(componentModel = "spring", uses = {FixtureMapper.class, TeamMapper.class})
public interface GameMapper extends EntityMapper<GameDTO, Game> {

    @Mapping(source = "fixture.id", target = "fixtureId")
    @Mapping(source = "teamA.id", target = "teamAId")
    @Mapping(source = "teamA.name", target = "teamAName")
    @Mapping(source = "teamB.id", target = "teamBId")
    @Mapping(source = "teamB.name", target = "teamBName")
    GameDTO toDto(Game game);

    @Mapping(source = "fixtureId", target = "fixture")
    @Mapping(source = "teamAId", target = "teamA")
    @Mapping(source = "teamBId", target = "teamB")
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
