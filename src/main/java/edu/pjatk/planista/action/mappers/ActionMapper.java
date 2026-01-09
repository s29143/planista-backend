package edu.pjatk.planista.action.mappers;

import edu.pjatk.planista.action.dto.ActionRequest;
import edu.pjatk.planista.action.dto.ActionResponse;
import edu.pjatk.planista.action.models.Action;
import edu.pjatk.planista.action.repositories.ActionTypeRepository;
import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.users.AppUserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { ActionDtoMapper.class, CompanyMapper.class, ContactMapper.class }
)
public abstract class ActionMapper {
    private AppUserRepository userRepository;

    private CompanyRepository companyRepository;

    private ContactRepository contactRepository;

    private ActionTypeRepository typeRepository;

    @Mappings({
            @Mapping(target = "user", source = "user", qualifiedByName = "userToDict"),
            @Mapping(target = "type", source = "type", qualifiedByName = "typeToDict"),
    })
    public abstract ActionResponse toResponse(Action entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "contact", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract Action toEntity(ActionRequest req);

    @AfterMapping
    protected void afterToEntity(ActionRequest req, @MappingTarget Action target) {
        if (req.userId() != null) {
            target.setUser(userRepository.getReferenceById(req.userId()));
        }
        if (req.companyId() != null) {
            target.setCompany(companyRepository.getReferenceById(req.companyId()));
        }
        if (req.contactId() != null) {
            target.setContact(contactRepository.getReferenceById(req.contactId()));
        }
        if (req.typeId() != null) {
            target.setType(typeRepository.getReferenceById(req.typeId()));
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "contact", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract void updateEntity(@MappingTarget Action target, ActionRequest req);

    @AfterMapping
    protected void afterUpdateEntity(ActionRequest req, @MappingTarget Action target) {
        if (req.userId() != null) {
            target.setUser(userRepository.getReferenceById(req.userId()));
        } else {
            target.setUser(null);
        }
        if (req.companyId() != null) {
            target.setCompany(companyRepository.getReferenceById(req.companyId()));
        } else {
            target.setCompany(null);
        }
        if (req.contactId() != null) {
            target.setContact(contactRepository.getReferenceById(req.contactId()));
        } else {
            target.setContact(null);
        }
        if (req.typeId() != null) {
            target.setType(typeRepository.getReferenceById(req.typeId()));
        } else {
            target.setType(null);
        }
    }

    @Autowired
    public void setUserRepository(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    public void setContactRepository(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Autowired
    public void setTypeRepository(ActionTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }
}
