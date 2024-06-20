package me.toddydev.brpayments.core.cache.categories;

import me.toddydev.brpayments.core.model.product.categories.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryCache {

    private List<Category> categories = new ArrayList<>();

    public void add(Category category) {
        categories.add(category);
    }

    public void remove(Category category) {
        categories.remove(category);
    }

    public Category find(String id) {
        return categories.stream().filter(category -> category.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
    public Category findIgnoreCase(String id) {
        return categories.stream().filter(category -> category.getId().equalsIgnoreCase(id)).findFirst().orElse(null);
    }


    public List<Category> getCategories() {
        return categories;
    }

}
