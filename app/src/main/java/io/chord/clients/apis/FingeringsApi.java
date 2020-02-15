package io.chord.clients.apis;

import io.reactivex.Observable;
import io.reactivex.Completable;
import retrofit2.http.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import io.chord.clients.models.Fingering;
import io.chord.clients.models.FingeringDto;
import io.chord.clients.models.ValidationProblemDetails;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

public interface FingeringsApi
{
    /**
    * @param body  (optional)
    * @return Observable&lt;Fingering&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/fingerings")
    Observable<Fingering> create(
        @retrofit2.http.Body FingeringDto body
    );

    /**
    * @param id  (required)
    * @return Completable
    */
    @DELETE("api/fingerings/{id}")
    Completable delete(
        @retrofit2.http.Path("id") String id
    );

    /**
    * @return Observable&lt;List&lt;Fingering&gt;&gt;
    */
    @GET("api/fingerings/all/by-author")
    Observable<List<Fingering>> getAllByAuthor();
        

    /**
    * @param id  (required)
    * @return Observable&lt;Fingering&gt;
    */
    @GET("api/fingerings/by-id/{id}")
    Observable<Fingering> getById(
        @retrofit2.http.Path("id") String id
    );

    /**
    * @param id  (required)
    * @param body  (optional)
    * @return Completable
    */
    @Headers({"Content-Type:application/json"})
    @PUT("api/fingerings/{id}")
    Completable update(
        @retrofit2.http.Path("id") String id,
        @retrofit2.http.Body FingeringDto body
    );
}
