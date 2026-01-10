package edu.pjatk.planista.order.mappers;

import edu.pjatk.planista.company.mappers.CompanyMapper;
import edu.pjatk.planista.contact.mappers.ContactMapper;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.order.repositories.OrderStatusRepository;
import edu.pjatk.planista.order.repositories.OrderTypeRepository;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import edu.pjatk.planista.shared.kernel.ports.CompanyQueryPort;
import edu.pjatk.planista.shared.kernel.ports.ContactQueryPort;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { OrderDtoMappers.class, CompanyMapper.class, ContactMapper.class }
)
public abstract class OrderMapper {
    private OrderStatusRepository statusRepository;

    private CompanyQueryPort companyQueryPort;

    private ContactQueryPort contactQueryPort;

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
            target.setCompany(companyQueryPort.getReferenceById(req.companyId()));
        }
        if (req.contactId() != null) {
            target.setContact(contactQueryPort.getReferenceById(req.contactId()));
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
    public void setStatusRepository(OrderStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
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
    public void setTypeRepository(OrderTypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }
}
