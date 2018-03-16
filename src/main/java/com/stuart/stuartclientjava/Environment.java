package com.stuart.stuartclientjava;

public enum Environment {

    SANDBOX("https://sandbox-api.stuart.com/oauth/token"),
    PRODUCTION("https://api.stuart.com/oauth/token");

    private final String baseUrl;

    Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    String baseUrl() {
        return baseUrl;
    }
}
