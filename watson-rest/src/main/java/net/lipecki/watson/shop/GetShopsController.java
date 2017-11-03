package net.lipecki.watson.shop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
