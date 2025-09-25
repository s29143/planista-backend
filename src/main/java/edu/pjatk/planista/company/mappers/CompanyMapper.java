package edu.pjatk.planista.company.mappers;

import edu.pjatk.planista.auth.AppUserRepository;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.company.repositories.CompanyAcquiredRepository;
import edu.pjatk.planista.company.repositories.CompanyStatusRepository;
import edu.pjatk.planista.company.repositories.CountryRepository;
import edu.pjatk.planista.company.repositories.DistrictRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { ClassDtoMappers.class }
)
public abstract class CompanyMapper {
    @Autowired
    protected AppUserRepository appUserRepository;

    @Autowired
    protected CompanyAcquiredRepository acquiredRepository;

    @Autowired
    protected DistrictRepository districtRepository;

    @Autowired
    protected CountryRepository countryRepository;

    @Autowired
    protected CompanyStatusRepository statusRepository;

    @Mappings({
            @Mapping(target = "user", source = "user", qualifiedByName = "userToDict"),
            @Mapping(target = "acquired", source = "acquired", qualifiedByName = "acquiredToDict"),
            @Mapping(target = "district", source = "district", qualifiedByName = "districtToDict"),
            @Mapping(target = "country", source = "country", qualifiedByName = "countryToDict"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "updatedAt", source = "updatedAt")
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
            @Mapping(target = "updatedBy", ignore = true)
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

    @BeanMapping(ignoreByDefault = false)
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
            @Mapping(target = "updatedBy", ignore = true)
    })
    public abstract void updateEntity(@MappingTarget Company target, CompanyRequest req);

    @AfterMapping
    protected void afterUpdateEntity(CompanyRequest req, @MappingTarget Company target) {
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
}
