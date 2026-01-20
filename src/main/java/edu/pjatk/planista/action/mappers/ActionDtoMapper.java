package edu.pjatk.planista.action.mappers;

import edu.pjatk.planista.action.models.ActionType;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.dto.IdNameDto;
import edu.pjatk.planista.shared.models.AppUser;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ActionDtoMapper {

    @Named("userToDict")
    public IdNameDto userToDict(AppUser u) {
        if (u == null) return null;
        return new IdNameDto(u.getId(), u.getUsername());
    }

    @Named("typeToDict")
    public DictItemDto typeToDict(ActionType s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName(), s.getColor());
    }

    @Named("companyToDict")
    public IdNameDto companyToDict(Company c) {
        return c == null ? null : new IdNameDto(c.getId(), c.getName());
    }

    @Named("contactToDict")
    public IdNameDto contactToDict(Contact c) {
        return c == null ? null : new IdNameDto(c.getId(), c.getName());
    }
}
