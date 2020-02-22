package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.FixtureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fixture} and its DTO {@link FixtureDTO}.
 */
@Mapper(componentModel = "spring", uses = {EventCategoryMapper.class})
public interface FixtureMapper extends EntityMapper<FixtureDTO, Fixture> {

    @Mapping(source = "eventCategory.id", target = "eventCategoryId")
    FixtureDTO toDto(Fixture fixture);

    @Mapping(source = "eventCategoryId", target = "eventCategory")
    Fixture toEntity(FixtureDTO fixtureDTO);

    default Fixture fromId(Long id) {
        if (id == null) {
            return null;
        }
        Fixture fixture = new Fixture();
        fixture.setId(id);
        return fixture;
    }
}
