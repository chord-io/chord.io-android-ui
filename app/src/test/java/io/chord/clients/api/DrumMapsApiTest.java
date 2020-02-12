package io.chord.clients.api;

import io.chord.clients.ApiClient;
import io.chord.clients.models.DrumMap;
import io.chord.clients.models.DrumMapDto;
import io.chord.clients.models.Fingering;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DrumMapsApi
 */
public class DrumMapsApiTest {

    private DrumMapsApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(DrumMapsApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void createTest() {
        DrumMapDto body = null;
        // DrumMap response = api.create(body);

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
    public void getAllTest() {
        // List<DrumMap> response = api.getAll();

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
        DrumMapDto body = null;
        // Void response = api.update(id, body);

        // TODO: test validations
    }
}
