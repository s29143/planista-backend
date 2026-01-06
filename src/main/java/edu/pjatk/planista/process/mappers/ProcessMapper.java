package edu.pjatk.planista.process.mappers;

import edu.pjatk.planista.order.mappers.OrderMapper;
import edu.pjatk.planista.order.repositories.OrderRepository;
import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.process.dto.ProcessResponse;
import edu.pjatk.planista.process.models.Process;
import edu.pjatk.planista.process.repositories.ProcessStatusRepository;
import edu.pjatk.planista.process.repositories.TechnologyRepository;
import edu.pjatk.planista.process.repositories.WorkstationRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = { ProcessClassDtoMappers.class, OrderMapper.class }
)
public abstract class ProcessMapper {

    @Autowired
    protected ProcessStatusRepository statusRepository;

    @Autowired
    protected WorkstationRepository workstationRepository;

    @Autowired
    protected TechnologyRepository technologyRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderMapper orderMapper;


    @Mappings({
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
            @Mapping(target = "technology", source = "technology", qualifiedByName = "technologyToDict"),
            @Mapping(target = "workstation", source = "workstation", qualifiedByName = "workstationToDict"),
            @Mapping(target = "order", source = "order"),
    })
    public abstract ProcessResponse toResponse(Process entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "order", ignore = true),
            @Mapping(target = "technology", ignore = true),
            @Mapping(target = "workstation", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract Process toEntity(ProcessRequest req);

    @AfterMapping
    protected void afterToEntity(ProcessRequest req, @MappingTarget Process target) {
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        }
        if (req.workstationId() != null) {
            target.setWorkstation(workstationRepository.getReferenceById(req.workstationId()));
        }
        if (req.technologyId() != null) {
            target.setTechnology(technologyRepository.getReferenceById(req.technologyId()));
        }
        if (req.orderId() != null) {
            target.setOrder(orderRepository.getReferenceById(req.orderId()));
        }
    }

    @BeanMapping(ignoreByDefault = false, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "workstation", ignore = true),
            @Mapping(target = "technology", ignore = true),
            @Mapping(target = "order", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "createdBy", ignore = true),
            @Mapping(target = "updatedBy", ignore = true),
            @Mapping(target = "createdByUser", ignore = true),
            @Mapping(target = "updatedByUser", ignore = true)
    })
    public abstract void updateEntity(@MappingTarget Process target, ProcessRequest req);

    @AfterMapping
    protected void afterUpdateEntity(ProcessRequest req, @MappingTarget Process target) {
        if (req.statusId() != null) {
            target.setStatus(statusRepository.getReferenceById(req.statusId()));
        } else {
            target.setStatus(null);
        }
        if (req.workstationId() != null) {
            target.setWorkstation(workstationRepository.getReferenceById(req.workstationId()));
        } else {
            target.setWorkstation(null);
        }
        if (req.technologyId() != null) {
            target.setTechnology(technologyRepository.getReferenceById(req.technologyId()));
        } else {
            target.setTechnology(null);
        }
        if (req.orderId() != null) {
            target.setOrder(orderRepository.getReferenceById(req.orderId()));
        } else {
            target.setOrder(null);
        }
    }
}
