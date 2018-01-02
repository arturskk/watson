package net.lipecki.watson.expanse;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.category.Category;
import net.lipecki.watson.rest.Api;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetExpansesController {

    private static final DateTimeFormatter fullDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final GetExpansesQuery query;

    public GetExpansesController(final GetExpansesQuery query) {
        this.query = query;
    }

    @GetMapping("/expanse")
    @Transactional
    public List<ExpanseDto> getExpanses(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate from,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate to) {
        log.debug("Request for expanses [from={}, to={}]", from, to);
        return this.query
                .getExpanses(from, to)
                .stream()
                .map(this::asDto)
                .collect(Collectors.toList());
    }

    private ExpanseDto asDto(final Expanse expanse) {
        return ExpanseDto
                .builder()
                .name(expanse.getName())
                .date(fullDateFormat.format(expanse.getDate()))
                .cost(expanse.getCost().getDescription())
                .category(
                        ExpanseDto
                                .ExpanseCategoryDto
                                .builder()
                                .uuid(expanse.getCategory().getUuid())
                                .name(expanse.getCategory().getName())
                                .parentUuid(expanse.getCategory().getParent().map(Category::getUuid).orElse(null))
                                .build()
                )
                .build();
    }

}
