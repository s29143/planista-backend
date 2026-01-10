package edu.pjatk.planista.action.mappers;

import edu.pjatk.planista.action.dto.ActionRequest;
import edu.pjatk.planista.action.models.Action;
import edu.pjatk.planista.action.repositories.ActionTypeRepository;
import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.shared.kernel.ports.CompanyQueryPort;
import edu.pjatk.planista.shared.kernel.ports.ContactQueryPort;
import edu.pjatk.planista.shared.kernel.ports.UserQueryPort;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { ActionDtoMapper.class, CompanyMapper.class, ContactMapper.class }
)
public abstract class ActionMapper {
    private UserQueryPort userQueryPort;

    private CompanyQueryPort companyQueryPort;

    private ContactQueryPort contactQueryPort;

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
            target.setUser(userQueryPort.getReferenceById(req.userId()));
        }
        if (req.companyId() != null) {
            target.setCompany(companyQueryPort.getReferenceById(req.companyId()));
        }
        if (req.contactId() != null) {
            target.setContact(contactQueryPort.getReferenceById(req.contactId()));
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
            target.setUser(userQueryPort.getReferenceById(req.userId()));
        } else {
            target.setUser(null);
        }
        if (req.companyId() != null) {
            target.setCompany(companyQueryPort.getReferenceById(req.companyId()));
        } else {
            target.setCompany(null);
        }
        if (req.contactId() != null) {
            target.setContact(contactQueryPort.getReferenceById(req.contactId()));
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
    public void setUserQueryPort(UserQueryPort userQueryPort) {
        this.userQueryPort = userQueryPort;
    }

    @Autowired
    public void setCompanyQueryPort(CompanyQueryPort companyQueryPort) {
        this.companyQueryPort = companyQueryPort;
    }

    @Autowired
    public void setContactQueryPort(ContactQueryPort contactQueryPort) {
        this.contactQueryPort = contactQueryPort;
    }

    @Autowired
    public void setTypeRepository(ActionTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }
}
