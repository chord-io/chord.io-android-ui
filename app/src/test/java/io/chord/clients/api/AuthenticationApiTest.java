package io.chord.clients.api;

import io.chord.clients.ApiClient;
import io.chord.clients.models.Authentication;
import io.chord.clients.models.SignInDto;
import io.chord.clients.models.User;
import io.chord.clients.models.UserDto;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for AuthenticationApi
 */
public class AuthenticationApiTest {

    private AuthenticationApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(AuthenticationApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void refreshTest() {
        String refreshToken = null;
        // Authentication response = api.refresh(refreshToken);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void signInTest() {
        SignInDto body = null;
        // Authentication response = api.signIn(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void signOutTest() {
        String refreshToken = null;
        // Void response = api.signOut(refreshToken);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void signUpTest() {
        UserDto body = null;
        // User response = api.signUp(body);

        // TODO: test validations
    }
}
