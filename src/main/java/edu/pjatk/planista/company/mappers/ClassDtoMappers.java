package edu.pjatk.planista.company.mappers;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.company.models.CompanyAcquired;
import edu.pjatk.planista.company.models.CompanyStatus;
import edu.pjatk.planista.company.models.Country;
import edu.pjatk.planista.company.models.District;
import edu.pjatk.planista.shared.dto.DictItem;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
public class ClassDtoMappers {

    @Named("userToDict")
    public DictItem userToDict(AppUser u) {
        if (u == null) return null;
        return new DictItem(u.getId(), u.getUsername());
    }

    @Named("acquiredToDict")
    public DictItem acquiredToDict(CompanyAcquired a) {
        return a == null ? null : new DictItem(a.getId(), a.getName());
    }

    @Named("districtToDict")
    public DictItem districtToDict(District d) {
        return d == null ? null : new DictItem(d.getId(), d.getName());
    }

    @Named("statusToDict")
    public DictItem statusToDict(CompanyStatus s) {
        return s == null ? null : new DictItem(s.getId(), s.getName());
    }

    @Named("countryToDict")
    public DictItem countryToDict(Country c) {
        return c == null ? null : new DictItem(c.getId(), c.getName());
    }
}
