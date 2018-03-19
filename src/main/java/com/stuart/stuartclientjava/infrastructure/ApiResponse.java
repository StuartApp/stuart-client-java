package com.stuart.stuartclientjava.infrastructure;

public class ApiResponse {

    private final int statusCode;
    private final String body;

    public ApiResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccessfull() {
        return statusCode == 200 || statusCode == 201;
    }
}
