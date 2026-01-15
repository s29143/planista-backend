package edu.pjatk.planista.shared.kernel.ports;


import edu.pjatk.planista.shared.kernel.dto.GanttItem;

import java.time.LocalDate;
import java.util.List;

public interface GanttProvider {
    List<GanttItem> getItems(LocalDate from, LocalDate to);
}