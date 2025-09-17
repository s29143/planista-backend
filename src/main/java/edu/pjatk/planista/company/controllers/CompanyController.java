package edu.pjatk.planista.company.controllers;

import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.company.dto.CompanyResponse;
import edu.pjatk.planista.company.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService service;

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.list(pageable));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody @Valid CompanyRequest request) {
        CompanyResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid CompanyRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
