package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.AddressDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class, UserExtraMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "userExtra.id", target = "userExtraId")
    AddressDTO toDto(Address address);

    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "userExtraId", target = "userExtra")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
