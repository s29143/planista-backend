package edu.pjatk.planista.order.mappers;

import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.company.repositories.CompanyRepository;
import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.contact.repositories.ContactRepository;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.order.repositories.OrderStatusRepository;
import edu.pjatk.planista.order.repositories.OrderTypeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { OrderDtoMappers.class, CompanyMapper.class, ContactMapper.class }
)
public abstract class OrderMapper {
    private OrderStatusRepository statusRepository;

    private CompanyRepository companyRepository;

    private ContactRepository contactRepository;

    private OrderTypeRepository typeRepository;


    @Mappings({
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
            @Mapping(target = "type", source = "type", qualifiedByName = "typeToDict"),
    })
    public abstract OrderResponse toResponse(Order entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
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
    public abstract Order toEntity(OrderRequest req);

    @AfterMapping
    protected void afterToEntity(OrderRequest req, @MappingTarget Order target) {
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
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

    @BeanMapping(ignoreByDefault = false, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
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
    public abstract void updateEntity(@MappingTarget Order target, OrderRequest req);

    @AfterMapping
    protected void afterUpdateEntity(OrderRequest req, @MappingTarget Order target) {
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        } else {
            target.setStatus(null);
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
    public void setStatusRepository(OrderStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
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
    public void setTypeRepository(OrderTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }
}
