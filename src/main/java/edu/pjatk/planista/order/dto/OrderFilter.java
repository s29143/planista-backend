package edu.pjatk.planista.order.dto;

import java.util.List;

public record OrderFilter(
        List<Long> statusIds,
        String company,
        String product,
        List<Long> typeIds
) { }
