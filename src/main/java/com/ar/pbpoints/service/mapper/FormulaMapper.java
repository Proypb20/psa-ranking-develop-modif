package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.FormulaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Formula} and its DTO {@link FormulaDTO}.
 */
@Mapper(componentModel = "spring", uses = {TournamentMapper.class})
public interface FormulaMapper extends EntityMapper<FormulaDTO, Formula> {

    @Mapping(source = "tournament.id", target = "tournamentId")
    @Mapping(source = "tournament.name", target = "tournamentName")
    FormulaDTO toDto(Formula formula);

    @Mapping(source = "tournamentId", target = "tournament")
    Formula toEntity(FormulaDTO formulaDTO);

    default Formula fromId(Long id) {
        if (id == null) {
            return null;
        }
        Formula formula = new Formula();
        formula.setId(id);
        return formula;
    }
}
