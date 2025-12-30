package edu.pjatk.planista.contact.controllers;

import edu.pjatk.planista.contact.dto.ContactFilter;
import edu.pjatk.planista.contact.dto.ContactRequest;
import edu.pjatk.planista.contact.dto.ContactResponse;
import edu.pjatk.planista.contact.services.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
public class ContactController {
    private final ContactService service;

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ContactResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                      @RequestParam(required = false) List<Long> userId,
                                                      @RequestParam(required = false) List<Long> statusId,
                                                      @RequestParam(required = false) String company,
                                                      @RequestParam(required = false) String search
                                                      ) {
        ContactFilter filter = new ContactFilter(userId, company, statusId, search);
        return ResponseEntity.ok(service.list(pageable, filter));
    }

    @PostMapping
    public ResponseEntity<ContactResponse> create(@RequestBody @Valid ContactRequest request) {
        ContactResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid ContactRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
