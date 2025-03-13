package org.artisan.infra.s3;

import java.util.Objects;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ImageUploadRequest(
        String imageUrl,
        ImageIdentifier imageIdentifier,
        Object data
){
    /**
     * path 뒤에 / 붙힐 것
     * ex) folder1/folder2/
     */
    @Builder
    public record ImageIdentifier(String path, String id) {
        public String key() {
            return path + id;
        }
    }
}
