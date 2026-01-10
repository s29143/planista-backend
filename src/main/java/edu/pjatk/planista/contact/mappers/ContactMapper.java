package edu.pjatk.planista.contact.mappers;

import edu.pjatk.planista.contact.dto.ContactRequest;
import edu.pjatk.planista.contact.models.Contact;
import edu.pjatk.planista.shared.kernel.dto.ContactResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { ContactClassDtoMappers.class }
)
public interface ContactMapper {

    @Mappings({
            @Mapping(target = "user", source = "user", qualifiedByName = "userToDict"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
            @Mapping(target = "company", source = "company", qualifiedByName = "companyToDict"),
    })
    ContactResponse toResponse(Contact entity);

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
    Contact toEntity(ContactRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
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
    void updateEntity(@MappingTarget Contact target, ContactRequest req);
}
