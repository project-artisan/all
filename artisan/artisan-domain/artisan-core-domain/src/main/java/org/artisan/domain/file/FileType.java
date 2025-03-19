package org.artisan.domain.file;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {
    AWS_STORAGE_URL("aws"),
    URL("url");


    private final String value;

    public static FileType from(String s) {
        var input = s.toLowerCase();
        return Arrays.stream(values())
                .filter(t -> t.getValue().equals(input))
                .findFirst()
                .orElseThrow();
    }
}
