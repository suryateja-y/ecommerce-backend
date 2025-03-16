package com.academy.projects.ecommerce.notificationservice.security;

public class TokenStorage {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setToken(String tokenToSet) {
        token.set(tokenToSet);
    }
}
