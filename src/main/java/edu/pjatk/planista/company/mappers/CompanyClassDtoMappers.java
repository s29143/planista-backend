package edu.pjatk.planista.company.mappers;

import edu.pjatk.planista.company.models.CompanyAcquired;
import edu.pjatk.planista.company.models.CompanyStatus;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.shared.dto.IdNameDto;
import edu.pjatk.planista.shared.models.AppUser;
import edu.pjatk.planista.shared.models.Country;
import edu.pjatk.planista.shared.models.District;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class CompanyClassDtoMappers {

    @Named("userToDict")
    public IdNameDto userToDict(AppUser u) {
        if (u == null) return null;
        return new IdNameDto(u.getId(), u.getUsername());
    }

    @Named("acquiredToDict")
    public DictItemDto acquiredToDict(CompanyAcquired a) {
        return a == null ? null : new DictItemDto(a.getId(), a.getName(), a.getColor());
    }

    @Named("districtToDict")
    public DictItemDto districtToDict(District d) {
        return d == null ? null : new DictItemDto(d.getId(), d.getName(), d.getColor());
    }

    @Named("statusToDict")
    public DictItemDto statusToDict(CompanyStatus s) {
        return s == null ? null : new DictItemDto(s.getId(), s.getName(), s.getColor());
    }

    @Named("countryToDict")
    public DictItemDto countryToDict(Country c) {
        return c == null ? null : new DictItemDto(c.getId(), c.getName(), c.getColor());
    }
}
