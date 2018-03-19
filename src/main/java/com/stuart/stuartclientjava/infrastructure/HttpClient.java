package com.stuart.stuartclientjava.infrastructure;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    protected OkHttpClient client;
    private Authenticator authenticator;

    public HttpClient(Authenticator authenticator) {
        this.authenticator = authenticator;
        this.client = new OkHttpClient();
    }

    public ApiResponse performGet(String resource) throws IOException {
        Request request = new Request.Builder()
                .url(getFullUrl(resource))
                .headers(Headers.of(getDefaultHeaders()))
                .build();

        Response response = client.newCall(request).execute();
        return new ApiResponse(response.code(), response.body().string());
    }

    public ApiResponse performPost(String resource, String body) throws IOException {
        Request request = new Request.Builder()
                .url(getFullUrl(resource))
                .headers(Headers.of(getDefaultHeaders()))
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body))
                .build();

        Response response = client.newCall(request).execute();
        return new ApiResponse(response.code(), response.body().string());
    }

    private Map<String, String> getDefaultHeaders() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("Authorization", String.format("Bearer %s", authenticator.getAccessToken()));
        result.put("User-Agent", String.format("stuart-client-java/%s", new Version().getCurrent()));
        result.put("Content-Type", "application/json");
        return result;
    }

    private String getFullUrl(String resource) {
        return String.format("%s%s", authenticator.getEnvironment().baseUrl(), resource);
    }
}
