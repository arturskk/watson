package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetCategoriesQuery {

    private final CategoryStore categoryStore;

    public GetCategoriesQuery(final CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    public List<Category> getCategories(final String categoryType) {
        final List<Category> categories = this.categoryStore.getCategories();
        return categories
                .stream()
                .filter(category -> category.isTypeOf(categoryType) || category.isRootCategory())
                .collect(Collectors.toList());
    }

}
