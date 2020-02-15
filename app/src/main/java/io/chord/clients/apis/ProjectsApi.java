package io.chord.clients.apis;

import java.util.List;

import io.chord.clients.models.Project;
import io.chord.clients.models.ProjectDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ProjectsApi
{
    /**
    * @param body  (optional)
    * @return Observable&lt;Project&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/projects")
    Observable<Project> create(
        @retrofit2.http.Body ProjectDto body
    );

    /**
    * @param id  (required)
    * @return Completable
    */
    @DELETE("api/projects/{id}")
    Completable delete(
        @retrofit2.http.Path("id") String id
    );

    /**
    * @return Observable&lt;List&lt;Project&gt;&gt;
    */
    @GET("api/projects/all/by-author")
    Observable<List<Project>> getAllByAuthor();
        

    /**
    * @param id  (required)
    * @return Observable&lt;Project&gt;
    */
    @GET("api/projects/by-id/{id}")
    Observable<Project> getById(
        @retrofit2.http.Path("id") String id
    );

    /**
    * @param id  (required)
    * @param body  (optional)
    * @return Completable
    */
    @Headers({"Content-Type:application/json"})
    @PUT("api/projects/{id}")
    Completable update(
        @retrofit2.http.Path("id") String id,
        @retrofit2.http.Body ProjectDto body
    );
}
