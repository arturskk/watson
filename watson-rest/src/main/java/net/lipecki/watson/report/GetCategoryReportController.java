package net.lipecki.watson.report;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.report.categoryexpanse.CategoryExpanseReport;
import net.lipecki.watson.report.categoryexpanse.GetCategoryExpanseReportQuery;
import net.lipecki.watson.rest.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetCategoryReportController {

    private final GetCategoryExpanseReportQuery query;

    public GetCategoryReportController(final GetCategoryExpanseReportQuery query) {
        this.query = query;
    }

    @GetMapping("/report/category")
    @Transactional
    public CategoryExpanseReport getCategoryReportDto(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate to) {
        log.debug("Request for categories report [from={}, to={}]", from, to);
        return this.query.getCategoryReport(from, to);
    }

}
