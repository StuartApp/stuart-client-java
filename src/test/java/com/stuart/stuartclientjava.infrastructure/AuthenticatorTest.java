package com.stuart.stuartclientjava.infrastructure;

import com.stuart.stuartclientjava.infrastructure.Authenticator;
import com.stuart.stuartclientjava.infrastructure.Environment;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class AuthenticatorTest {

    @Test
    void shouldReturnTheCurrentValidAccessTokenWhenAlreadyExisting() {
        // given
        Authenticator authenticator = spy(new Authenticator(Environment.SANDBOX, "sample-id", "sample-secret"));
        doReturn(validAccessToken("new-access-token")).when(authenticator).getNewAccessToken();
        authenticator.getAccessToken();

        // when
        String accessToken = authenticator.getAccessToken();

        // then
        assertThat(accessToken).isEqualTo("new-access-token");
    }

    @Test
    void shouldReturnNewAccessTokenWhenNoExistingToken() {
        // given
        Authenticator authenticator = spy(new Authenticator(Environment.SANDBOX, "sample-id", "sample-secret"));
        doReturn(validAccessToken("new-access-token")).when(authenticator).getNewAccessToken();

        // when
        String accessToken = authenticator.getAccessToken();

        // then
        assertThat(accessToken).isEqualTo("new-access-token");
    }

    @Test
    void shouldReturnNewAccessTokenWhenCurrentIsExpired() {
        // given
        Authenticator authenticator = spy(new Authenticator(Environment.SANDBOX, "sample-id", "sample-secret"));
        doReturn(expiredAccessToken("expired-token")).when(authenticator).getNewAccessToken();
        authenticator.getNewAccessToken();

        // when
        doReturn(validAccessToken("new-access-token")).when(authenticator).getNewAccessToken();
        String accessToken = authenticator.getAccessToken();

        // then
        assertThat(accessToken).isEqualTo("new-access-token");
    }

    public static OAuth2AccessToken validAccessToken(final String token) {
        return new OAuth2AccessToken() {
            @Override
            public Map<String, Object> getAdditionalInformation() {
                return null;
            }

            @Override
            public Set<String> getScope() {
                return null;
            }

            @Override
            public OAuth2RefreshToken getRefreshToken() {
                return null;
            }

            @Override
            public String getTokenType() {
                return null;
            }

            @Override
            public boolean isExpired() {
                return false;
            }

            @Override
            public Date getExpiration() {
                return null;
            }

            @Override
            public int getExpiresIn() {
                return 999999999;
            }

            @Override
            public String getValue() {
                return token;
            }
        };
    }

    private OAuth2AccessToken expiredAccessToken(final String token) {
        return new OAuth2AccessToken() {
            @Override
            public Map<String, Object> getAdditionalInformation() {
                return null;
            }

            @Override
            public Set<String> getScope() {
                return null;
            }

            @Override
            public OAuth2RefreshToken getRefreshToken() {
                return null;
            }

            @Override
            public String getTokenType() {
                return null;
            }

            @Override
            public boolean isExpired() {
                return true;
            }

            @Override
            public Date getExpiration() {
                return null;
            }

            @Override
            public int getExpiresIn() {
                return 999999999;
            }

            @Override
            public String getValue() {
                return token;
            }
        };
    }
}
