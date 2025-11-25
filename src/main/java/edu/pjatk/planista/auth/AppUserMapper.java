package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.UserDto;
import edu.pjatk.planista.users.dto.UserRequest;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring"
)
public abstract class AppUserMapper {
    public abstract UserDto toResponse(AppUser entity);

    @Mappings({
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "authorities", ignore = true)
    })
    public abstract AppUser toEntity(UserDto req);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "authorities", ignore = true)
    })
    public abstract AppUser toEntity(UserRequest req);


    @BeanMapping(ignoreByDefault = false, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "authorities", ignore = true)
    })
    public abstract void updateEntity(@MappingTarget AppUser target, UserRequest req);
}
