package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.DocTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocType} and its DTO {@link DocTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DocTypeMapper extends EntityMapper<DocTypeDTO, DocType> {



    default DocType fromId(Long id) {
        if (id == null) {
            return null;
        }
        DocType docType = new DocType();
        docType.setId(id);
        return docType;
    }
}
