package com.psa.ranking.service.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.psa.ranking.domain.Authority;
import com.psa.ranking.domain.UserExtra;
import com.psa.ranking.service.dto.UserExtraDTO;
import com.psa.ranking.web.rest.vm.ManagedUserVM;

/**
 * Mapper for the entity {@link UserExtra} and its DTO {@link UserExtraDTO}.
 */
@Mapper(componentModel = "spring", uses = {DocTypeMapper.class, UserMapper.class})
public interface UserExtraMapper extends EntityMapper<UserExtraDTO, UserExtra> {

    @Mapping(source = "docType.id", target = "docTypeId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    UserExtraDTO toDto(UserExtra userExtra);

    @Mapping(source = "docTypeId", target = "docType")
    @Mapping(source = "userId", target = "user")
    UserExtra toEntity(UserExtraDTO userExtraDTO);

    default UserExtra fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserExtra userExtra = new UserExtra();
        userExtra.setId(id);
        return userExtra;
    }
    
    @Mapping(source = "user.activated",target = "activated")
	@Mapping(source = "user.authorities",target = "authorities", qualifiedByName = "lalala")
	@Mapping(source = "user.createdBy",target = "createdBy")
	@Mapping(source = "user.createdDate",target = "createdDate")
	@Mapping(source = "user.email",target = "email")
	@Mapping(source = "user.firstName",target = "firstName")
	@Mapping(source = "user.imageUrl",target = "imageUrl")
	@Mapping(source = "user.langKey",target = "langKey")
	@Mapping(source = "user.lastModifiedBy",target = "lastModifiedBy")
	@Mapping(source = "user.lastModifiedDate",target = "lastModifiedDate")
	@Mapping(source = "user.lastName",target = "lastName")
	@Mapping(source = "user.login",target = "login")
	@Mapping(source = "user.password",target = "password")
	ManagedUserVM toManagedUserVM (UserExtra userExtra);

    @Named("lalala")
    default Set<String> authoritiesFromStrings(Set<Authority> authoritiesAsString) {
        Set<String> authorities = new HashSet<>();

        if(authoritiesAsString != null){
            authorities = authoritiesAsString.stream().map(x -> {
                return x.getName();
            }).collect(Collectors.toSet());
        }

        return authorities;
    }
}
