package com.academy.projects.ecommerce.inventorymanagementservice.security;

public class TokenManager {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setToken(String tokenToSet) {
        token.set(tokenToSet);
    }
    public static String getToken() {
        return token.get();
    }
}
