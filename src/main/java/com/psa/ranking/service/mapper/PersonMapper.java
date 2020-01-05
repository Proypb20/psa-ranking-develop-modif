package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.PersonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class, DocTypeMapper.class})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "docType.id", target = "docTypeId")
    PersonDTO toDto(Person person);

    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "docTypeId", target = "docType")
    Person toEntity(PersonDTO personDTO);

    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
