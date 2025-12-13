package edu.pjatk.planista.contact.dto;

import java.util.List;

public record ContactFilter(
        List<Long> userIds,
        String company,
        List<Long> statusIds,
        String search
) { }
