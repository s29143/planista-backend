package edu.pjatk.planista.process.dto;

import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Duration;
import java.time.Instant;

public record ProcessResponse(
        Long id,
        Long quantity,
        Long plannedTimeSeconds,
        Instant createdAt,
        Instant updatedAt,
        OrderResponse order,
        DictItemDto technology,
        DictItemDto workstation,
        DictItemDto status
) {}