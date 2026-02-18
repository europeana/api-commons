package eu.europeana.api.commons.oauth2.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class OAuthUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"Bearer ABC", "bearer ABC", "BEARER ", "bEARER ABC", "bearer      ", "bEArER "})
    public void testisBearerToken_validTokenType(String bearerPrefix) {
        Assertions.assertTrue(OAuthUtils.validateBearerToken(bearerPrefix));
    }

    @ParameterizedTest
    @ValueSource(strings = {"random ", "bEARER", "  Bearer", "", "Bea rer"})
    public void testisBearerToken_invalidTokenTypeValue(String bearerPrefix) {
        Assertions.assertFalse(OAuthUtils.validateBearerToken(bearerPrefix));
    }

    @ParameterizedTest
    @EmptySource
    public void testisBearerToken_emptyTokenType(String bearerPrefix) {
        Assertions.assertFalse(OAuthUtils.validateBearerToken(bearerPrefix));
    }

    @ParameterizedTest
    @NullSource
    public void testisBearerToken_null(String bearerPrefix) {
        Assertions.assertFalse(OAuthUtils.validateBearerToken(bearerPrefix));
    }
}