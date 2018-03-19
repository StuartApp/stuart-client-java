package com.stuart.stuartclientjava.infrastructure;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

    public void creatJob() throws IOException {
        Authenticator authenticator = new Authenticator(Environment.SANDBOX, "c6058849d0a056fc743203acb8e6a850dad103485c3edc51b16a9260cc7a7688", "aa6a415fce31967501662c1960fcbfbf4745acff99acb19dbc1aae6f76c9c619");

        HttpClient client = new HttpClient(authenticator);

        JsonObject root = new JsonObject();
        JsonObject job = new JsonObject();
        root.add("job", job);

        job.addProperty("transport_type", "bike");
        JsonObject pickup = new JsonObject();
        pickup.addProperty("address", "12 rue rivoli, 75001 Paris");
        JsonArray pickups = new JsonArray();
        pickups.add(pickup);

        job.add("pickups", pickups);


        JsonObject dropoff = new JsonObject();
        dropoff.addProperty("address", "42 rue rivoli, 75001 Paris");
        JsonArray dropoffs = new JsonArray();
        dropoffs.add(dropoff);

        job.add("dropoffs", dropoffs);

        String jobAsJson = new Gson().toJson(root);
        ApiResponse apiResponse = client.performPost("/v2/jobs", jobAsJson);
        apiResponse.getBody();
    }

    @Test
    void shouldSendGetRequestWithCorrectParameters() throws IOException {
        this.httpClient.performGet("/sample-endpoint");

        Request request = this.okHttpClient.getRequest();
        assertThat(request.url().toString()).isEqualTo("https://sandbox-api.stuart.com/sample-endpoint");
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
        this.httpClient.performPost("/sample-endpoint", "{'some': 'body'}");

        Request request = this.okHttpClient.getRequest();
        assertThat(request.url().toString()).isEqualTo("https://sandbox-api.stuart.com/sample-endpoint");
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
