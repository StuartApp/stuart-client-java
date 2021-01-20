package com.stuart.stuartclientjava.infrastructure;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class HttpClientTest {

    private OkHttpClientMock okHttpClient;
    private HttpClientMock httpClient;

    @BeforeEach
    void setUp() {
        Authenticator authenticator = spy(new Authenticator(Environment.SANDBOX, "sample-id", "sample-secret"));
        doReturn(AuthenticatorTest.validAccessToken("token")).when(authenticator).getNewAccessToken();

        this.okHttpClient = new OkHttpClientMock();
        this.httpClient = new HttpClientMock(authenticator, okHttpClient);
    }

    @Test
    void shouldSendGetRequestWithCorrectParameters() throws IOException {
        // when
        this.httpClient.performGet("/sample-endpoint");
        Request request = this.okHttpClient.getRequest();

        // then
        assertThat(request.url().toString()).isEqualTo("https://api.sandbox.stuart.com/sample-endpoint");
        assertThat(request.method()).isEqualTo("GET");
        assertThat(request.headers()).isEqualTo(
                Headers.of(
                        "Authorization", "Bearer token",
                        "User-Agent", String.format("stuart-client-java/%s", new Version().getCurrent()),
                        "Content-Type", "application/json"
                )
        );
        assertThat(request.body()).isNull();
    }

    @Test
    void shouldSendPostRequestWithCorrectParameters() throws IOException {
        // when
        this.httpClient.performPost("/sample-endpoint", "{'some': 'body'}");
        Request request = this.okHttpClient.getRequest();

        // then
        assertThat(request.url().toString()).isEqualTo("https://api.sandbox.stuart.com/sample-endpoint");
        assertThat(request.method()).isEqualTo("POST");
        assertThat(request.headers()).isEqualTo(
                Headers.of(
                        "Authorization", "Bearer token",
                        "User-Agent", String.format("stuart-client-java/%s", new Version().getCurrent()),
                        "Content-Type", "application/json"
                )
        );

        Buffer sink = new Buffer();
        request.body().writeTo(sink);
        assertThat(sink.readUtf8()).isEqualTo("{'some': 'body'}");
    }

    @Test
    void shouldSendPatchRequestWithCorrectParameters() throws IOException {
        // when
        this.httpClient.performPatch("/sample-endpoint", "{'some': 'body'}");
        Request request = this.okHttpClient.getRequest();

        // then
        assertThat(request.url().toString()).isEqualTo("https://api.sandbox.stuart.com/sample-endpoint");
        assertThat(request.method()).isEqualTo("PATCH");
        assertThat(request.headers()).isEqualTo(
                Headers.of(
                        "Authorization", "Bearer token",
                        "User-Agent", String.format("stuart-client-java/%s", new Version().getCurrent()),
                        "Content-Type", "application/json"
                )
        );

        Buffer sink = new Buffer();
        request.body().writeTo(sink);
        assertThat(sink.readUtf8()).isEqualTo("{'some': 'body'}");
    }

    private class HttpClientMock extends HttpClient {

        public HttpClientMock(Authenticator authenticator, OkHttpClient client) {
            super(authenticator);
            this.client = client;
        }
    }

    private class OkHttpClientMock extends OkHttpClient {

        private Request request;

        @Override
        public Call newCall(Request request) {
            this.request = request;
            return super.newCall(request);
        }

        public Request getRequest() {
            return this.request;
        }
    }
}
