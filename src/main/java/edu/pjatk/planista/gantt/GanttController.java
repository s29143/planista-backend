package edu.pjatk.planista.gantt;

import edu.pjatk.planista.shared.kernel.dto.GanttItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gantt")
public class GanttController {
    private final GanttService ganttService;

    @GetMapping
    public List<GanttItem> gantt(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return ganttService.gantt(from, to);
    }
}
