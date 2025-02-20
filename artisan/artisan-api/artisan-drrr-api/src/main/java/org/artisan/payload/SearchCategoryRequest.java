package org.artisan.payload;


public record SearchCategoryRequest(
        String name,
        String constant
) {
}