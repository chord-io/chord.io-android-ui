package io.chord.clients.api;

import io.chord.clients.ApiClient;
import java.util.UUID;
import io.chord.clients.models.UserDto;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UsersApi
 */
public class UsersApiTest {

    private UsersApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(UsersApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void deleteTest() {
        UUID id = null;
        // Void response = api.delete(id);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void updateTest() {
        UUID id = null;
        UserDto body = null;
        // Void response = api.update(id, body);

        // TODO: test validations
    }
}
