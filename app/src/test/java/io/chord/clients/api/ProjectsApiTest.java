package io.chord.clients.api;

import io.chord.clients.ApiClient;
import io.chord.clients.models.Project;
import io.chord.clients.models.ProjectDto;
import io.chord.clients.models.ValidationProblemDetails;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ProjectsApi
 */
public class ProjectsApiTest {

    private ProjectsApi api;

    @Before
    public void setup() {
        api = new ApiClient().createService(ProjectsApi.class);
    }


    /**
     * 
     *
     * 
     */
    @Test
    public void createTest() {
        ProjectDto body = null;
        // Project response = api.create(body);

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
        // List<Project> response = api.getAllByAuthor();

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
        // Project response = api.getById(id);

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
        ProjectDto body = null;
        // Void response = api.update(id, body);

        // TODO: test validations
    }
}
