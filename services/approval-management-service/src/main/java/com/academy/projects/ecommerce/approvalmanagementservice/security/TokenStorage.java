package com.academy.projects.ecommerce.approvalmanagementservice.security;

public class TokenStorage {

    private static final ThreadLocal<String> token = new ThreadLocal<>();

    public static void setToken(String tokenToSet) {
        token.set(tokenToSet);
    }
    public static String getToken() {
        return token.get();
    }
}
