package edu.pjatk.planista.order.dto;

import java.time.LocalDate;

public record OrderRequest(
        String product,
        LocalDate dateFrom,
        LocalDate dateTo,
        Long quantity,

        Long companyId,
        Long contactId,
        Long statusId,
        Long typeId
) {
}
