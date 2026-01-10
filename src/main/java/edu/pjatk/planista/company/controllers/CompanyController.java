package edu.pjatk.planista.company.controllers;

import edu.pjatk.planista.shared.kernel.dto.ActionResponse;
import edu.pjatk.planista.company.dto.CompanyFilter;
import edu.pjatk.planista.company.dto.CompanyRequest;
import edu.pjatk.planista.shared.kernel.dto.CompanyResponse;
import edu.pjatk.planista.company.services.CompanyActionService;
import edu.pjatk.planista.company.services.CompanyContactService;
import edu.pjatk.planista.company.services.CompanyOrderService;
import edu.pjatk.planista.company.services.CompanyService;
import edu.pjatk.planista.shared.kernel.dto.ContactResponse;
import edu.pjatk.planista.shared.kernel.dto.OrderResponse;
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
@RequestMapping("/api/v1/companies")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService service;
    private final CompanyContactService companyContactService;
    private final CompanyActionService companyActionService;
    private final CompanyOrderService companyOrderService;
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                      @RequestParam(required = false) List<Long> userId,
                                                      @RequestParam(required = false) List<Long> statusId,
                                                      @RequestParam(required = false) List<Long> districtId,
                                                      @RequestParam(required = false) String search
                                                      ) {
        CompanyFilter filter = new CompanyFilter(userId, districtId, statusId, search);
        return ResponseEntity.ok(service.list(pageable, filter));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@RequestBody @Valid CompanyRequest request) {
        CompanyResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{companyId}/contacts")
    public Page<ContactResponse> getCompanyContacts(
            @PathVariable Long companyId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return companyContactService.getContacts(companyId, pageable);
    }

    @GetMapping("/{companyId}/actions")
    public Page<ActionResponse> getCompanyActions(
            @PathVariable Long companyId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return companyActionService.getActions(companyId, pageable);
    }

    @GetMapping("/{companyId}/orders")
    public Page<OrderResponse> getCompanyOrders(
            @PathVariable Long companyId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return companyOrderService.getOrders(companyId, pageable);
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
