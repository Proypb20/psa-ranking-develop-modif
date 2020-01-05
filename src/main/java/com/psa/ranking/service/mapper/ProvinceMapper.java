package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.ProvinceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Province} and its DTO {@link ProvinceDTO}.
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class})
public interface ProvinceMapper extends EntityMapper<ProvinceDTO, Province> {

    @Mapping(source = "country.id", target = "countryId")
    ProvinceDTO toDto(Province province);

    @Mapping(source = "countryId", target = "country")
    @Mapping(target = "cities", ignore = true)
    @Mapping(target = "removeCity", ignore = true)
    Province toEntity(ProvinceDTO provinceDTO);

    default Province fromId(Long id) {
        if (id == null) {
            return null;
        }
        Province province = new Province();
        province.setId(id);
        return province;
    }
}
