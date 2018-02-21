package net.lipecki.watson.shop;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.rest.Api;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(Api.V1)
public class GetShopsController {

    private final GetShopsQuery query;

    public GetShopsController(final GetShopsQuery query) {
        this.query = query;
    }

    @GetMapping("/shop")
    @Transactional
    public List<ListShopDto> getShops() {
        return query.getShops()
                .stream()
                .map(this::asListShopDto)
                .collect(Collectors.toList());
    }

    private ListShopDto asListShopDto(final Shop shop) {
        return ListShopDto.builder()
                .name(shop.getName())
                .uuid(shop.getUuid())
                .retailChain(
                        shop.getRetailChainOptional()
                                .map(
                                        chain -> ListShopDto.RetailChainDto
                                                .builder()
                                                .name(chain.getName())
                                                .uuid(chain.getUuid())
                                                .build()
                                )
                                .orElse(null)
                )
                .build();
    }

}
