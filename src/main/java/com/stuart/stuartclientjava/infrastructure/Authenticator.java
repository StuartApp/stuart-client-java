package com.stuart.stuartclientjava.infrastructure;

import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class Authenticator {

    private final ClientCredentialsResourceDetails clientCredentialsResourceDetails;
    private final Environment environment;
    private OAuth2AccessToken oAuth2AccessToken;

    public Authenticator(Environment environment, String apiClientId, String apiClientSecret) {
        ClientCredentialsResourceDetails clientCredentialsResourceDetails = new ClientCredentialsResourceDetails();
        clientCredentialsResourceDetails.setAccessTokenUri(String.format("%s/oauth/token", environment.baseUrl()));
        clientCredentialsResourceDetails.setClientId(apiClientId);
        clientCredentialsResourceDetails.setClientSecret(apiClientSecret);
        this.clientCredentialsResourceDetails = clientCredentialsResourceDetails;
        this.environment = environment;
    }

    public String getAccessToken() {
        if (oAuth2AccessToken != null && !oAuth2AccessToken.isExpired()) {
            return oAuth2AccessToken.getValue();
        } else {
            this.oAuth2AccessToken = getNewAccessToken();
            return this.oAuth2AccessToken.getValue();
        }
    }

    public OAuth2AccessToken getNewAccessToken() {
        return new ClientCredentialsAccessTokenProvider().obtainAccessToken(clientCredentialsResourceDetails, new DefaultAccessTokenRequest());
    }

    public Environment getEnvironment() {
        return environment;
    }
}
