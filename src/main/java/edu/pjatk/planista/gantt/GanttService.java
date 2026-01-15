package edu.pjatk.planista.gantt;

import edu.pjatk.planista.shared.kernel.dto.GanttItem;
import edu.pjatk.planista.shared.kernel.ports.GanttProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GanttService {

    private final List<GanttProvider> providers;

    @Transactional(readOnly = true)
    public List<GanttItem> gantt(LocalDate from, LocalDate to) {
        if (from == null || to == null) throw new IllegalArgumentException("from/to required");
        if (from.isAfter(to)) throw new IllegalArgumentException("from > to");

        return providers.stream()
                .flatMap(p -> p.getItems(from, to).stream())
                .sorted(Comparator.comparing(GanttItem::type).thenComparing(GanttItem::id))
                .toList();
    }
}