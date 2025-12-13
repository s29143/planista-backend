package edu.pjatk.planista.contact.mappers;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.contact.models.ContactStatus;
import edu.pjatk.planista.shared.dto.DictItemDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ContactClassDtoMappers {

    @Named("userToDict")
    public DictItemDto userToDict(AppUser u) {
        if (u == null) return null;
        return new DictItemDto(u.getId(), u.getUsername());
    }

    @Named("statusToDict")
    public DictItemDto statusToDict(ContactStatus s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }
}
