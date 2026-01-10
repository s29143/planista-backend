package edu.pjatk.planista.action.mappers;

import edu.pjatk.planista.action.dto.ActionRequest;
import edu.pjatk.planista.action.models.Action;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { ActionDtoMapper.class })
public interface ActionMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    Action toEntity(ActionRequest req);

    @Mapping(target = "user", source = "user", qualifiedByName = "userToDict")
    @Mapping(target = "type", source = "type", qualifiedByName = "typeToDict")
    @Mapping(target = "company", source = "company", qualifiedByName = "companyToDict")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactToDict")
    ActionResponse toResponse(Action entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "contact", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    void updateEntity(@MappingTarget Action target, ActionRequest req);
}
