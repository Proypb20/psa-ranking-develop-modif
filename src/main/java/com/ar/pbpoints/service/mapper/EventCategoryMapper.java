package com.ar.pbpoints.service.mapper;

import com.ar.pbpoints.domain.*;
import com.ar.pbpoints.service.dto.EventCategoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventCategory} and its DTO {@link EventCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {EventMapper.class, CategoryMapper.class, FormatMapper.class})
public interface EventCategoryMapper extends EntityMapper<EventCategoryDTO, EventCategory> {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "event.name", target = "eventName")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "format.id", target = "formatId")
    @Mapping(source = "format.name", target = "formatName")
    EventCategoryDTO toDto(EventCategory eventCategory);

    @Mapping(source = "eventId", target = "event")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "formatId", target = "format")
    @Mapping(target = "games", ignore = true)
    @Mapping(target = "removeGame", ignore = true)
    EventCategory toEntity(EventCategoryDTO eventCategoryDTO);

    default EventCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        EventCategory eventCategory = new EventCategory();
        eventCategory.setId(id);
        return eventCategory;
    }
}
