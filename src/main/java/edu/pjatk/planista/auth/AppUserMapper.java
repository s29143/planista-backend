package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
}
