package edu.pjatk.planista.order.controllers;

import edu.pjatk.planista.order.dto.OrderFilter;
import edu.pjatk.planista.order.dto.OrderRequest;
import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.order.services.OrderProcessService;
import edu.pjatk.planista.order.services.OrderService;
import edu.pjatk.planista.process.dto.ProcessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    private final OrderProcessService orderProcessService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> list(@PageableDefault(size = 20, sort = "id") Pageable pageable,
                                                     @RequestParam(required = false) String product,
                                                     @RequestParam(required = false) List<Long> statusId,
                                                     @RequestParam(required = false) String company,
                                                     @RequestParam(required = false) String search
                                                      ) {
        OrderFilter filter = new OrderFilter(statusId, product, company, search);
        return ResponseEntity.ok(service.list(pageable, filter));
    }

    @GetMapping("/{orderId}/proccesses")
    public Page<ProcessResponse> getCompanyOrders(
            @PathVariable Long orderId,
            Pageable pageable
    ) {
        return orderProcessService.getProcesses(orderId, pageable);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid OrderRequest request) {
        OrderResponse created = service.create(request);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                 @RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
