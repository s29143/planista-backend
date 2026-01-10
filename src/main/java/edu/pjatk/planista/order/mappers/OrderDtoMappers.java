package edu.pjatk.planista.order.mappers;

import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.contact.models.Contact;
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

    @Named("companyToDict")
    public DictItemDto companyToDict(Company c) {
        return c == null ? null : new DictItemDto(c.getId(), c.getName());
    }

    @Named("contactToDict")
    public DictItemDto contactToDict(Contact c) {
        return c == null ? null : new DictItemDto(c.getId(), c.getName());
    }
}
