package io.chord.clients.apis;

import io.reactivex.Observable;
import io.reactivex.Completable;
import retrofit2.http.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import io.chord.clients.models.DrumMap;
import io.chord.clients.models.DrumMapDto;
import io.chord.clients.models.Fingering;
import io.chord.clients.models.ValidationProblemDetails;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

public interface DrumMapsApi
{
    /**
    * @param body  (optional)
    * @return Observable&lt;DrumMap&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/drum-maps")
    Observable<DrumMap> create(
        @retrofit2.http.Body DrumMapDto body
    );

    /**
    * @param id  (required)
    * @return Completable
    */
    @DELETE("api/drum-maps/{id}")
    Completable delete(
        @retrofit2.http.Path("id") String id
    );

    /**
    * @return Observable&lt;List&lt;DrumMap&gt;&gt;
    */
    @GET("api/drum-maps/all/by-author")
    Observable<List<DrumMap>> getAll();
        

    /**
    * @param id  (required)
    * @return Observable&lt;Fingering&gt;
    */
    @GET("api/drum-maps/by-id/{id}")
    Observable<Fingering> getById(
        @retrofit2.http.Path("id") String id
    );

    /**
    * @param id  (required)
    * @param body  (optional)
    * @return Completable
    */
    @Headers({"Content-Type:application/json"})
    @PUT("api/drum-maps/{id}")
    Completable update(
        @retrofit2.http.Path("id") String id,
        @retrofit2.http.Body DrumMapDto body
    );
}
