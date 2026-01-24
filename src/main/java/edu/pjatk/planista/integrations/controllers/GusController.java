package edu.pjatk.planista.integrations.controllers;

import edu.pjatk.planista.integrations.models.CompanyFromRegonDto;
import edu.pjatk.planista.integrations.services.GusRegonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/integrations/gus")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
class GusController {

    private final GusRegonService gusRegonService;

    @GetMapping("/regon/by-nip/{nip}")
    public ResponseEntity<CompanyFromRegonDto> byNip(@PathVariable String nip) {
        return ResponseEntity.ok(gusRegonService.findByNip(nip));
    }
}