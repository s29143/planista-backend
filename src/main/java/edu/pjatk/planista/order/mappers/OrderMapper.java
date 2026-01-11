package edu.pjatk.planista.order.mappers;

import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.models.Order;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { OrderDtoMappers.class }
)
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
            @Mapping(target = "type", source = "type", qualifiedByName = "typeToDict"),
            @Mapping(target = "company", source = "company", qualifiedByName = "companyToDict"),
            @Mapping(target = "contact", source = "contact", qualifiedByName = "contactToDict"),
    })
    OrderResponse toResponse(Order entity);

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
    Order toEntity(OrderRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
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
    void updateEntity(@MappingTarget Order target, OrderRequest req);
}
