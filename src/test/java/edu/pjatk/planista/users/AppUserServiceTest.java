package edu.pjatk.planista.users;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.auth.AppUserMapper;
import edu.pjatk.planista.auth.dto.UserDto;
import edu.pjatk.planista.company.dto.CompanyFilter;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.shared.dto.DictItemDto;
import edu.pjatk.planista.users.dto.UserRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

class AppUserServiceTest {

    private AppUserRepository appUserRepository;
    private AppUserMapper mapper;
    private AppUserService service;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        mapper = mock(AppUserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);
        service = new AppUserService(appUserRepository, mapper, passwordEncoder);
    }

    @Test
    void create_shouldMapEncodePasswordSave_andReturnResponse() {
        // given
        UserRequest req = new UserRequest(
                "jdoe",
                "John",
                "Doe",
                "Ch@ngeM3123!@#",
                null
        );

        AppUser entityToSave = new AppUser();
        AppUser saved = new AppUser();
        saved.setId(42L);

        UserDto response = new UserDto(
                42L,
                "jdoe",
                "John",
                "Doe",
                null
        );

        given(mapper.toEntity(req)).willReturn(entityToSave);
        given(passwordEncoder.encode("Ch@ngeM3123!@#")).willReturn("encoded-password");
        given(appUserRepository.save(entityToSave)).willReturn(saved);
        given(mapper.toResponse(saved)).willReturn(response);

        // when
        UserDto result = service.create(req);

        // then
        assertThat(result.id()).isEqualTo(42L);
        then(mapper).should().toEntity(req);
        then(passwordEncoder).should().encode("Ch@ngeM3123!@#");
        then(appUserRepository).should().save(entityToSave);
        then(mapper).should().toResponse(saved);
    }

    @Test
    void update_shouldUpdateManagedEntity_andReturnResponse() {
        // given
        Long id = 10L;
        UserRequest req = new UserRequest(
                "jdoe",
                "John",
                "Doe",
                null,
                null
        );

        AppUser existing = new AppUser();
        existing.setId(id);

        UserDto response = new UserDto(
                id,
                "jdoe",
                "John",
                "Doe",
                null
        );

        given(appUserRepository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        UserDto result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(appUserRepository).should(never()).save(any());
        then(passwordEncoder).should(never()).encode(any());
    }

    @Test
    void update_shouldEncodePassword_whenProvided() {
        // given
        Long id = 11L;
        UserRequest req = new UserRequest(
                "jdoe",
                "John",
                "Doe",
                "NewP@ssw0rd!",
                null
        );

        AppUser existing = new AppUser();
        existing.setId(id);

        UserDto response = new UserDto(
                id,
                "jdoe",
                "John",
                "Doe",
                null
        );

        given(appUserRepository.findById(id)).willReturn(Optional.of(existing));
        willDoNothing().given(mapper).updateEntity(existing, req);
        given(passwordEncoder.encode("NewP@ssw0rd!")).willReturn("encoded-new");
        given(mapper.toResponse(existing)).willReturn(response);

        // when
        UserDto result = service.update(id, req);

        // then
        assertThat(result.id()).isEqualTo(id);
        then(mapper).should().updateEntity(existing, req);
        then(passwordEncoder).should().encode("NewP@ssw0rd!");
        then(appUserRepository).should(never()).save(any());
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        // given
        Long id = 999L;
        UserRequest req = new UserRequest(
                "jdoe",
                "John",
                "Doe",
                null,
                null
        );
        given(appUserRepository.findById(id)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.update(id, req))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User " + id + " not found");
    }

    @Test
    void get_shouldReturnResponse() {
        Long id = 5L;
        AppUser entity = new AppUser();
        entity.setId(id);

        UserDto resp = new UserDto(
                id,
                "jdoe",
                "John",
                "Doe",
                null
        );

        given(appUserRepository.findById(id)).willReturn(Optional.of(entity));
        given(mapper.toResponse(entity)).willReturn(resp);

        UserDto result = service.get(id);

        assertThat(result.id()).isEqualTo(id);
        then(appUserRepository).should().findById(id);
        then(mapper).should().toResponse(entity);
    }

    @Test
    void get_shouldThrow_whenNotFound() {
        given(appUserRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void delete_shouldRemove_whenExists() {
        Long id = 7L;
        given(appUserRepository.existsById(id)).willReturn(true);

        service.delete(id);

        then(appUserRepository).should().deleteById(id);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        Long id = 8L;
        given(appUserRepository.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User " + id + " not found");
    }

    @Test
    void list_shouldReturnPagedResponses() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());
        String search = "john";

        AppUser u1 = new AppUser(); u1.setId(1L);
        AppUser u2 = new AppUser(); u2.setId(2L);
        Page<AppUser> page = new PageImpl<>(List.of(u1, u2), pageable, 2);

        UserDto r1 = new UserDto(1L, "u1", "John", "Doe", null);
        UserDto r2 = new UserDto(2L, "u2", "Jane", "Doe", null);

        given(appUserRepository.findAll(any(Specification.class), eq(pageable))).willReturn(page);
        given(mapper.toResponse(u1)).willReturn(r1);
        given(mapper.toResponse(u2)).willReturn(r2);

        Page<UserDto> result = service.list(pageable, search);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting(UserDto::id)
                .containsExactly(1L, 2L);
    }
}
