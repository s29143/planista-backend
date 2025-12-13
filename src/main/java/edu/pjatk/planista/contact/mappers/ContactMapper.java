package edu.pjatk.planista.contact.mappers;

import edu.pjatk.planista.auth.AuthRepository;
import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.contact.dto.ContactRequest;
import edu.pjatk.planista.contact.dto.ContactResponse;
import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.contact.repositories.ContactStatusRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { ContactClassDtoMappers.class, CompanyMapper.class }
)
public abstract class ContactMapper {
    @Autowired
    protected AuthRepository authRepository;

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected ContactStatusRepository statusRepository;

    @Autowired
    protected CompanyMapper companyMapper;

    @Mappings({
            @Mapping(target = "user", source = "user", qualifiedByName = "userToDict"),
            @Mapping(target = "company", source = "company"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
    })
    public abstract ContactResponse toResponse(Contact entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract Contact toEntity(ContactRequest req);

    @AfterMapping
    protected void afterToEntity(ContactRequest req, @MappingTarget Contact target) {
        if (req.userId() != null) {
            target.setUser(authRepository.getReferenceById(req.userId()));
        }
        if (req.companyId() != null) {
            target.setCompany(companyRepository.getReferenceById(req.companyId()));
        }
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        }
    }

    @BeanMapping(ignoreByDefault = false, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract void updateEntity(@MappingTarget Contact target, ContactRequest req);

    @AfterMapping
    protected void afterUpdateEntity(ContactRequest req, @MappingTarget Contact target) {
        if (req.userId() != null) {
            target.setUser(authRepository.getReferenceById(req.userId()));
        } else {
            target.setUser(null);
        }
        if (req.companyId() != null) {
            target.setCompany(companyRepository.getReferenceById(req.companyId()));
        } else {
            target.setCompany(null);
        }
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        } else {
            target.setStatus(null);
        }
    }
}
