package edu.pjatk.planista.order.mappers;

import edu.pjatk.planista.order.models.OrderStatus;
import edu.pjatk.planista.order.models.OrderType;
import edu.pjatk.planista.shared.dto.DictItemDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoMappers {
    @Named("typeToDict")
    public DictItemDto typeToDict(OrderType s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }

    @Named("statusToDict")
    public DictItemDto statusToDict(OrderStatus s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }
}
