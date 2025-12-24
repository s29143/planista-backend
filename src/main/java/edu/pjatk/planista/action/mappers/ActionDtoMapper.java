package edu.pjatk.planista.action.mappers;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.action.models.ActionType;
import edu.pjatk.planista.shared.dto.DictItemDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ActionDtoMapper {

    @Named("userToDict")
    public DictItemDto userToDict(AppUser u) {
        if (u == null) return null;
        return new DictItemDto(u.getId(), u.getUsername());
    }

    @Named("typeToDict")
    public DictItemDto typeToDict(ActionType s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }
}
