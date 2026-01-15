package edu.pjatk.planista.process.mappers;

import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.shared.kernel.dto.GanttItem;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import org.mapstruct.*;

import java.time.LocalDate;

@Mapper(
        componentModel = "spring",
        uses = { ProcessClassDtoMappers.class }
)
public interface ProcessMapper {

    @Mappings({
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
            @Mapping(target = "technology", source = "technology", qualifiedByName = "technologyToDict"),
            @Mapping(target = "workstation", source = "workstation", qualifiedByName = "workstationToDict"),
            @Mapping(target = "order", source = "order", qualifiedByName = "orderToDict")
    })
    ProcessResponse toResponse(Process entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "order", ignore = true),
            @Mapping(target = "technology", ignore = true),
            @Mapping(target = "workstation", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    Process toEntity(ProcessRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "workstation", ignore = true),
            @Mapping(target = "technology", ignore = true),
            @Mapping(target = "order", ignore = true),

            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    void updateEntity(@MappingTarget Process target, ProcessRequest req);


    @Mapping(target = "id", expression = "java(\"PROCESS-\" + entity.getId())")
    @Mapping(target = "text", expression = "java(entity.getName())")
    @Mapping(target = "start", source = "dateFrom")
    @Mapping(target = "end", source = "entity", qualifiedByName = "processEnd")
    @Mapping(target = "type", constant = "PROCESS")
    @Mapping(target = "parentId", expression = "java(\"ORDER-\" + entity.getOrder().getId())")
    GanttItem toGanttItem(Process entity);


    @Named("processEnd")
    default LocalDate processEnd(Process p) {
        if (p.getDateFrom() == null) return null;
        return p.getDateTo() != null
                ? p.getDateTo()
                : p.getDateFrom().plusDays(1);
    }
}
