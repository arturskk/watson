package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class GetCategoryQuery {

    private final CategoryStore categoryStore;

    public GetCategoryQuery(final CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    public Optional<Category> getCategory(final String uuid) {
        final Map<String, Category> categories = this.categoryStore.getCategories();
        return Optional.ofNullable(categories.get(uuid));
    }

    public Category getRootCategory() {
        final Map<String, Category> categories = this.categoryStore.getCategories();
        return categories.get(Category.ROOT_UUID);
    }

}
