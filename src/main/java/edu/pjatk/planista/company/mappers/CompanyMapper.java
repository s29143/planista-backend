package edu.pjatk.planista.company.mappers;

import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.company.repositories.CompanyAcquiredRepository;
import edu.pjatk.planista.company.repositories.CompanyStatusRepository;
import edu.pjatk.planista.shared.repositories.CountryRepository;
import edu.pjatk.planista.shared.repositories.DistrictRepository;
import edu.pjatk.planista.users.AppUserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { CompanyClassDtoMappers.class }
)
public abstract class CompanyMapper {
    private AppUserRepository appUserRepository;

    private CompanyAcquiredRepository acquiredRepository;

    private DistrictRepository districtRepository;

    private CountryRepository countryRepository;

    private CompanyStatusRepository statusRepository;

    @Mappings({
            @Mapping(target = "user", source = "user", qualifiedByName = "userToDict"),
            @Mapping(target = "acquired", source = "acquired", qualifiedByName = "acquiredToDict"),
            @Mapping(target = "district", source = "district", qualifiedByName = "districtToDict"),
            @Mapping(target = "country", source = "country", qualifiedByName = "countryToDict"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
    })
    public abstract CompanyResponse toResponse(Company entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "acquired", ignore = true),
            @Mapping(target = "district", ignore = true),
            @Mapping(target = "country", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract Company toEntity(CompanyRequest req);

    @AfterMapping
    protected void afterToEntity(CompanyRequest req, @MappingTarget Company target) {
        if (req.userId() != null) {
            target.setUser(appUserRepository.getReferenceById(req.userId()));
        }
        if (req.acquiredId() != null) {
            target.setAcquired(acquiredRepository.getReferenceById(req.acquiredId()));
        }
        if (req.districtId() != null) {
            target.setDistrict(districtRepository.getReferenceById(req.districtId()));
        }
        if (req.countryId() != null) {
            target.setCountry(countryRepository.getReferenceById(req.countryId()));
        }
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "acquired", ignore = true),
            @Mapping(target = "district", ignore = true),
            @Mapping(target = "country", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract void updateEntity(@MappingTarget Company target, CompanyRequest req);

    @AfterMapping
    protected void afterUpdateEntity(CompanyRequest req, @MappingTarget Company target) {
        if (req.userId() != null) {
            target.setUser(appUserRepository.getReferenceById(req.userId()));
        } else {
            target.setUser(null);
        }
        if (req.acquiredId() != null) {
            target.setAcquired(acquiredRepository.getReferenceById(req.acquiredId()));
        } else {
            target.setAcquired(null);
        }
        if (req.districtId() != null) {
            target.setDistrict(districtRepository.getReferenceById(req.districtId()));
        } else {
            target.setDistrict(null);
        }
        if (req.countryId() != null) {
            target.setCountry(countryRepository.getReferenceById(req.countryId()));
        } else {
            target.setCountry(null);
        }
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        } else {
            target.setStatus(null);
        }
    }

    @Autowired
    public void setAppUserRepository(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Autowired
    public void setAcquiredRepository(CompanyAcquiredRepository acquiredRepository) {
        this.acquiredRepository = acquiredRepository;
    }

    @Autowired
    public void setDistrictRepository(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Autowired
    public void setStatusRepository(CompanyStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }
}
