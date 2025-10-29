package edu.pjatk.planista.company.dto;

import java.util.List;

public record CompanyFilter(
        List<Long> userIds,
        List<Long> statusIds,
        String search
) { }
