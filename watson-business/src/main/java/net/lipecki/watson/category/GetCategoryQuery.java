package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import net.lipecki.watson.WatsonException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class GetCategoryQuery {

    private final CategoryStore categoryStore;

    public GetCategoryQuery(final CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    public Optional<Category> getCategory(final String uuid) {
        return this.categoryStore.getCategory(uuid);
    }

    public Category getRootCategory() {
        return this.categoryStore.getCategory(Category.ROOT_UUID).orElseThrow(WatsonException.supplier("Missing root category!"));
    }

}
