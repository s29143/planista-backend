package edu.pjatk.planista.shared.kernel.dto;

import java.time.LocalDate;

public record GanttItem(
        String id,
        String text,
        LocalDate start,
        LocalDate end,
        String type,
        String parentId
) {}