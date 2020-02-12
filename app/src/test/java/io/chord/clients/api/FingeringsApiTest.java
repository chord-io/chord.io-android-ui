package io.chord.clients.api;

import io.chord.clients.ApiClient;
import io.chord.clients.models.Fingering;
import io.chord.clients.models.FingeringDto;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for FingeringsApi
 */
public class FingeringsApiTest {

    private FingeringsApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(FingeringsApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void createTest() {
        FingeringDto body = null;
        // Fingering response = api.create(body);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void deleteTest() {
        String id = null;
        // Void response = api.delete(id);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void getAllByAuthorTest() {
        // List<Fingering> response = api.getAllByAuthor();

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void getByIdTest() {
        String id = null;
        // Fingering response = api.getById(id);

        // TODO: test validations
    }

    /**
     * 
     *
     * 
     */
    @Test
    public void updateTest() {
        String id = null;
        FingeringDto body = null;
        // Void response = api.update(id, body);

        // TODO: test validations
    }
}
