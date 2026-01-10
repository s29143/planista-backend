package edu.pjatk.planista.process.controllers;

import edu.pjatk.planista.shared.kernel.dto.ExecutionResponse;
import edu.pjatk.planista.process.dto.ProcessRequest;
import edu.pjatk.planista.shared.kernel.dto.ProcessResponse;
import edu.pjatk.planista.process.services.ProcessExecutionService;
import edu.pjatk.planista.process.services.ProcessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/processes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','PLANNER', 'PRODUCTION')")
public class ProcessController {
    private final ProcessService service;
    private final ProcessExecutionService processExecutionService;

    @GetMapping("/{id}")
    public ResponseEntity<ProcessResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProcessResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.list(pageable));
    }

    @GetMapping("/{processId}/orders")
    public Page<ExecutionResponse> getProcessExecutions(
            @PathVariable Long processId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return processExecutionService.getProcesses(processId, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PLANNER')")
    public ResponseEntity<ProcessResponse> create(@RequestBody @Valid ProcessRequest request) {
        ProcessResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLANNER')")
    public ResponseEntity<ProcessResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid ProcessRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PLANNER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
