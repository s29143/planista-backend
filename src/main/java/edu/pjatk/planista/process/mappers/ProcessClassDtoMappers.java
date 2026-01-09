package edu.pjatk.planista.process.mappers;

import edu.pjatk.planista.process.models.ProcessStatus;
import edu.pjatk.planista.shared.models.Technology;
import edu.pjatk.planista.shared.models.Workstation;
import edu.pjatk.planista.shared.dto.DictItemDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ProcessClassDtoMappers {
    @Named("statusToDict")
    public DictItemDto statusToDict(ProcessStatus s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }

    @Named("technologyToDict")
    public DictItemDto technologyToDict(Technology s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }

    @Named("workstationToDict")
    public DictItemDto workstationToDict(Workstation s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }
}
