package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.UserExtraDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, DocTypeMapper.class})
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "docType.id", target = "docTypeId")
    UserExtraDTO toDto(UserExtra userExtra);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "docTypeId", target = "docType")
    UserExtra toEntity(UserExtraDTO userExtraDTO);

    default UserExtra fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserExtra userExtra = new UserExtra();
        userExtra.setId(id);
        return userExtra;
    }
}
