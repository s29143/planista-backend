package edu.pjatk.planista.execution.mappers;

import edu.pjatk.planista.execution.dto.ExecutionRequest;
import edu.pjatk.planista.execution.models.Execution;
import edu.pjatk.planista.process.mappers.ProcessMapper;
import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import edu.pjatk.planista.shared.kernel.ports.ProcessQueryPort;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { ProcessMapper.class }
)
public abstract class ExecutionMapper {
    private ProcessQueryPort processQueryPort;

    public abstract ExecutionResponse toResponse(Execution entity);

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
    public abstract Execution toEntity(ExecutionRequest req);

    @AfterMapping
    protected void afterToEntity(ExecutionRequest req, @MappingTarget Execution target) {
        if (req.processId() != null) {
            target.setProcess(processQueryPort.getReferenceById(req.processId()));
        }
    }

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
    public abstract void updateEntity(@MappingTarget Execution target, ExecutionRequest req);

    @AfterMapping
    protected void afterUpdateEntity(ExecutionRequest req, @MappingTarget Execution target) {
        if (req.processId() != null) {
            target.setProcess(processQueryPort.getReferenceById(req.processId()));
        } else {
            target.setProcess(null);
        }
    }

    @Autowired
    public void setProcessQueryPort(ProcessQueryPort processQueryPort) {
        this.processQueryPort = processQueryPort;
    }
}
