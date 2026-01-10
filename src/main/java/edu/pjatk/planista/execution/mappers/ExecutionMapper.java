package edu.pjatk.planista.execution.mappers;

import edu.pjatk.planista.execution.dto.ExecutionRequest;
import edu.pjatk.planista.execution.models.Execution;
import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { ExecutionClassDtoMapper.class }
)
public interface ExecutionMapper {
    @Mapping(target = "process", source = "process", qualifiedByName = "processToDict")
    ExecutionResponse toResponse(Execution entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "process", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    Execution toEntity(ExecutionRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "process", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    void updateEntity(@MappingTarget Execution target, ExecutionRequest req);
}
