package edu.pjatk.planista.users;

import edu.pjatk.planista.auth.AppUser;
import edu.pjatk.planista.auth.AppUserMapper;
import edu.pjatk.planista.auth.dto.UserDto;
import edu.pjatk.planista.company.models.Company;
import edu.pjatk.planista.users.dto.UserRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.pjatk.planista.users.UserSpecs.searchLike;


@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final AppUserMapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public Page<UserDto> list(Pageable pageable, String search) {
        Specification<AppUser> spec = Specification.allOf(
                searchLike(search)
        );
        return appUserRepository.findAll(spec, pageable).map(mapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UserDto get(Long id) {
        return mapper.toResponse(appUserRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    public UserDto create(UserRequest req) {
        AppUser entity = mapper.toEntity(req);
        if (req.password() != null && !req.password().isBlank()) {
            entity.setPassword(passwordEncoder.encode(req.password()));
        }
        AppUser saved = appUserRepository.save(entity);
        return mapper.toResponse(saved);
    }

    public UserDto update(Long id, UserRequest req) {
        AppUser entity = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User " + id + " not found"));
        mapper.updateEntity(entity, req);
        if (req.password() != null && !req.password().isBlank()) {
            entity.setPassword(passwordEncoder.encode(req.password()));
        }
        return mapper.toResponse(entity);
    }

    public void delete(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException("User " + id + " not found");
        }
        appUserRepository.deleteById(id);
    }
}
