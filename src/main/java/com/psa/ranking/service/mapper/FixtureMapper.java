package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.FixtureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fixture} and its DTO {@link FixtureDTO}.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, CategoryMapper.class})
public interface FixtureMapper extends EntityMapper<FixtureDTO, Fixture> {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    FixtureDTO toDto(Fixture fixture);

    @Mapping(source = "eventId", target = "event")
    @Mapping(source = "categoryId", target = "category")
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
