package net.lipecki.watson.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetCategoriesQuery {

    private final CategoryStore categoryStore;

    public GetCategoriesQuery(final CategoryStore categoryStore) {
        this.categoryStore = categoryStore;
    }

    public List<Category> getCategories(final String categoryType) {
        final Map<String, Category> categories = this.categoryStore.getCategories();
        return categories.values()
                .stream()
                .filter(category -> categoryType.equals(category.getType()))
                .collect(Collectors.toList());
    }

}
