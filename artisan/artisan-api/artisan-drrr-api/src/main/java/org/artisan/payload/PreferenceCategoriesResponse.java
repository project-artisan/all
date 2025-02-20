package org.artisan.payload;

import java.util.List;

public record PreferenceCategoriesResponse(
        List<SearchCategoryResponse> categories
) {


}

