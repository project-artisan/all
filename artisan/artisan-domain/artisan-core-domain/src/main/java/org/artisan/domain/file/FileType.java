package org.artisan.domain.file;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FileType {
    URL("url");


    private final String value;

    public static FileType from(String s) {
        return Arrays.stream(values())
                .filter(t -> t.name().equals(s))
                .findFirst()
                .orElseThrow();
    }
}
