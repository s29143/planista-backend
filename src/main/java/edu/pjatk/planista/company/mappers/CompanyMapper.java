package edu.pjatk.planista.company.mappers;

import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.shared.kernel.dto.CompanyResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        uses = { CompanyClassDtoMappers.class }
)
public interface CompanyMapper {

    @Mappings({
            @Mapping(target = "user", source = "user", qualifiedByName = "userToDict"),
            @Mapping(target = "acquired", source = "acquired", qualifiedByName = "acquiredToDict"),
            @Mapping(target = "district", source = "district", qualifiedByName = "districtToDict"),
            @Mapping(target = "country", source = "country", qualifiedByName = "countryToDict"),
            @Mapping(target = "status", source = "status", qualifiedByName = "statusToDict"),
    })
    CompanyResponse toResponse(Company entity);

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
    Company toEntity(CompanyRequest req);

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
    void updateEntity(@MappingTarget Company target, CompanyRequest req);
}
