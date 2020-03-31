package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.TeamPointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamPoint} and its DTO {@link TeamPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {TeamMapper.class, TournamentMapper.class})
public interface TeamPointMapper extends EntityMapper<TeamPointDTO, TeamPoint> {

    @Mapping(source = "team.id", target = "teamId")
    @Mapping(source = "team.name", target = "teamName")
    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    TeamPointDTO toDto(TeamPoint teamPoint);

    @Mapping(source = "teamId", target = "team")
    @Mapping(source = "tournamentId", target = "tournament")
    TeamPoint toEntity(TeamPointDTO teamPointDTO);

    default TeamPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        TeamPoint teamPoint = new TeamPoint();
        teamPoint.setId(id);
        return teamPoint;
    }
}
