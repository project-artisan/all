package org.artisan.payload;

import java.util.List;

public record PreferenceUpdateRequest(
        List<Long> categoryIds
) {
}
