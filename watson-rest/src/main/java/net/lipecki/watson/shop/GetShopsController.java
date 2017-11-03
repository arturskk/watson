package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetShopsController {

    private final GetShopsQuery query;

    public GetShopsController(final GetShopsQuery query) {
        this.query = query;
    }

    @GetMapping("/shop")
    public List<Shop> getShops() {
        return this.query.getShops();
    }

}
