package edu.pjatk.planista.execution.mappers;

import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.dto.IdNameDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public class ExecutionClassDtoMapper {

    @Named("processToDict")
    public IdNameDto processToDict(Process p) {
        if (p == null) return null;
        return new IdNameDto(p.getId(), p.getName());
    }
}