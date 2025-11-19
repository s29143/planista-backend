package edu.pjatk.planista.company.mappers;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.company.models.CompanyAcquired;
import edu.pjatk.planista.company.models.CompanyStatus;
import edu.pjatk.planista.company.models.Country;
import edu.pjatk.planista.company.models.District;
import edu.pjatk.planista.shared.dto.DictItemDto;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ClassDtoMappers {

    @Named("userToDict")
    public DictItemDto userToDict(AppUser u) {
        if (u == null) return null;
        return new DictItemDto(u.getId(), u.getUsername());
    }

    @Named("acquiredToDict")
    public DictItemDto acquiredToDict(CompanyAcquired a) {
        return a == null ? null : new DictItemDto(a.getId(), a.getName());
    }

    @Named("districtToDict")
    public DictItemDto districtToDict(District d) {
        return d == null ? null : new DictItemDto(d.getId(), d.getName());
    }

    @Named("statusToDict")
    public DictItemDto statusToDict(CompanyStatus s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName());
    }

    @Named("countryToDict")
    public DictItemDto countryToDict(Country c) {
        return c == null ? null : new DictItemDto(c.getId(), c.getName());
    }
}
