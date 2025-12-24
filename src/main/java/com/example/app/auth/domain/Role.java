package com.example.app.auth.domain;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    WORKER("ROLE_WORKER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
