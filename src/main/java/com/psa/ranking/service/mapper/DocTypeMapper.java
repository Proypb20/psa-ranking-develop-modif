package com.psa.ranking.service.mapper;

import com.psa.ranking.domain.*;
import com.psa.ranking.service.dto.DocTypeDTO;

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
