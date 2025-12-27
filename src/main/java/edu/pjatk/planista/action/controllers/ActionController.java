package edu.pjatk.planista.action.controllers;

import edu.pjatk.planista.action.services.ActionService;
import edu.pjatk.planista.action.dto.ActionFilter;
import edu.pjatk.planista.action.dto.ActionResponse;
import edu.pjatk.planista.action.dto.ActionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/actions")
@RequiredArgsConstructor
public class ActionController {
    private final ActionService service;

    @GetMapping("/{id}")
    public ResponseEntity<ActionResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<ActionResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                     @RequestParam(required = false) List<Long> userId,
                                                     @RequestParam(required = false) List<Long> typeId,
                                                     @RequestParam(required = false) String company,
                                                     @RequestParam(required = false) String search
                                                      ) {
        ActionFilter filter = new ActionFilter(userId, company, typeId, search);
        return ResponseEntity.ok(service.list(pageable, filter));
    }

    @PostMapping
    public ResponseEntity<ActionResponse> create(@RequestBody @Valid ActionRequest request) {
        ActionResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActionResponse> update(@PathVariable Long id,
                                                 @RequestBody @Valid ActionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
