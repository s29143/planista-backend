package edu.pjatk.planista.action.dto;

import java.util.List;

public record ActionFilter(
        List<Long> userIds,
        String company,
        List<Long> typeIds,
        String search
) { }
