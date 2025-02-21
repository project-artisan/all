package org.artisan.core;

public record User(Long id, Role role){
    public boolean isMember() {
        return role == Role.MEMBER;
    }

    public static User member(Long id) {
        return new User(id , Role.MEMBER);
    }

    public static User guest() {
        return new User(null, Role.GUEST);
    }
}
