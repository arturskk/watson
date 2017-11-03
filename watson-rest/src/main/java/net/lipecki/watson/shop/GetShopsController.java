package net.lipecki.watson.shop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GetShopsController {

    @GetMapping("/shop")
    public List<Shop> getShops() {
        return new ArrayList<>();
    }

}
