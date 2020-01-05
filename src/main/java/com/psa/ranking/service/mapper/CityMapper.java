package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.CityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProvinceMapper.class})
public interface CityMapper extends EntityMapper<CityDTO, City> {

    @Mapping(source = "province.id", target = "provinceId")
    CityDTO toDto(City city);

    @Mapping(source = "provinceId", target = "province")
    City toEntity(CityDTO cityDTO);

    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }
}
