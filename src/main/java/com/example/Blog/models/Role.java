package com.example.Blog.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, FISH_MODERATOR, POST_MODERATOR, COMMENT_MODERATOR;

    @Override
    public String getAuthority() {
        return name();
    }
}