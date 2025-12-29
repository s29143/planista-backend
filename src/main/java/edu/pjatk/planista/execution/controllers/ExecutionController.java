package edu.pjatk.planista.execution.controllers;

import edu.pjatk.planista.execution.dto.ExecutionRequest;
import edu.pjatk.planista.execution.dto.ExecutionResponse;
import edu.pjatk.planista.execution.services.ExecutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/executions")
@RequiredArgsConstructor
public class ExecutionController {
    private final ExecutionService service;

    @GetMapping("/{id}")
    public ResponseEntity<ExecutionResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ExecutionResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.list(pageable));
    }

    @PostMapping
    public ResponseEntity<ExecutionResponse> create(@RequestBody @Valid ExecutionRequest request) {
        ExecutionResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExecutionResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid ExecutionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
