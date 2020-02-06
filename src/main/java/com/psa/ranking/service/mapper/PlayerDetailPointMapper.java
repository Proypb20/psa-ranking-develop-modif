package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.PlayerDetailPointDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlayerDetailPoint} and its DTO {@link PlayerDetailPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, PlayerPointMapper.class})
public interface PlayerDetailPointMapper extends EntityMapper<PlayerDetailPointDTO, PlayerDetailPoint> {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "playerPoint.id", target = "playerPointId")
    PlayerDetailPointDTO toDto(PlayerDetailPoint playerDetailPoint);

    @Mapping(source = "eventId", target = "event")
    @Mapping(source = "playerPointId", target = "playerPoint")
    PlayerDetailPoint toEntity(PlayerDetailPointDTO playerDetailPointDTO);

    default PlayerDetailPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlayerDetailPoint playerDetailPoint = new PlayerDetailPoint();
        playerDetailPoint.setId(id);
        return playerDetailPoint;
    }
}
