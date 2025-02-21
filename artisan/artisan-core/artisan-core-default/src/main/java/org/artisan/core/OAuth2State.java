package org.artisan.core;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuth2State {
    GITHUB("github"),

    ;

    private final String value;
}
