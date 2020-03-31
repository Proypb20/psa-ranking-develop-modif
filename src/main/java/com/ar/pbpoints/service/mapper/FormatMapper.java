package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.FormatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Format} and its DTO {@link FormatDTO}.
 */
@Mapper(componentModel = "spring", uses = {TournamentMapper.class})
public interface FormatMapper extends EntityMapper<FormatDTO, Format> {

    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    FormatDTO toDto(Format format);

    @Mapping(source = "tournamentId", target = "tournament")
    Format toEntity(FormatDTO formatDTO);

    default Format fromId(Long id) {
        if (id == null) {
            return null;
        }
        Format format = new Format();
        format.setId(id);
        return format;
    }
}
