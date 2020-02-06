package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.TeamDetailPointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TeamDetailPoint} and its DTO {@link TeamDetailPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {TeamPointMapper.class, EventMapper.class})
public interface TeamDetailPointMapper extends EntityMapper<TeamDetailPointDTO, TeamDetailPoint> {

    @Mapping(source = "teamPoint.id", target = "teamPointId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    TeamDetailPointDTO toDto(TeamDetailPoint teamDetailPoint);

    @Mapping(source = "teamPointId", target = "teamPoint")
    @Mapping(source = "eventId", target = "event")
    TeamDetailPoint toEntity(TeamDetailPointDTO teamDetailPointDTO);

    default TeamDetailPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        TeamDetailPoint teamDetailPoint = new TeamDetailPoint();
        teamDetailPoint.setId(id);
        return teamDetailPoint;
    }
}
