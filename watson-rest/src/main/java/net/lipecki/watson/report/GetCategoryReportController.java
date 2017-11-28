package net.lipecki.watson.report;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetCategoryReportController {

    private final GetCategoryReportQuery query;

    public GetCategoryReportController(final GetCategoryReportQuery query) {
        this.query = query;
    }

    @GetMapping("/report/category")
    public CategoryReport getCategoryReportDto(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate from,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate to) {
        log.debug("Request for categories report [from={}, to={}]", from, to);
        return this.query.getCategoryReport(from, to);
    }

}
