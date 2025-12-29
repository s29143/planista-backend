package edu.pjatk.planista.process.dto;

import edu.pjatk.planista.order.dto.OrderResponse;
import edu.pjatk.planista.shared.dto.DictItemDto;

import java.time.Duration;

public record ProcessResponse(
        Long id,
        Integer quantity,
        Duration plannedTime,
        OrderResponse order,
        DictItemDto technology,
        DictItemDto workstation,
        DictItemDto status
) {}