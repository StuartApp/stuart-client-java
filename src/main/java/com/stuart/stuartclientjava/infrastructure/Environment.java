package com.stuart.stuartclientjava.infrastructure;

public enum Environment {

    SANDBOX("https://api.sandbox.stuart.com"),
    PRODUCTION("https://api.stuart.com");

    private final String baseUrl;

    Environment(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    String baseUrl() {
        return baseUrl;
    }
}
