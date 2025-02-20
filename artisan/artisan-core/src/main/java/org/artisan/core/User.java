package org.artisan.core;

public record User(Long id, Role role){
    public boolean isMember() {
        return role == Role.MEMBER;
    }
}
