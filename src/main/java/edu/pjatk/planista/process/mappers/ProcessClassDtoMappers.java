package edu.pjatk.planista.process.mappers;

import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.process.models.ProcessStatus;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.models.Technology;
import edu.pjatk.planista.shared.models.Workstation;
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

    @Named("orderToDict")
    public DictItemDto orderToDict(Order o) {
        return o == null ? null : new DictItemDto(o.getId(), o.getName());
    }
}
