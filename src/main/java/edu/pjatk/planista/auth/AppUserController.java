package edu.pjatk.planista.auth;

import edu.pjatk.planista.auth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(appUserService.list(pageable));
    }
}
