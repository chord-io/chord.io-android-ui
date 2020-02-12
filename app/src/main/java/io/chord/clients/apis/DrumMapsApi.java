package io.chord.clients.apis;

import java.util.List;

import io.chord.clients.models.DrumMap;
import io.chord.clients.models.DrumMapDto;
import io.chord.clients.models.Fingering;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DrumMapsApi {
  /**
   * 
   * 
   * @param body  (optional)
   * @return Call&lt;DrumMap&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/drum-maps")
  Observable<DrumMap> create(
                    @retrofit2.http.Body DrumMapDto body    
  );

  /**
   * 
   * 
   * @param id  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("api/drum-maps/{id}")
  Observable<Void> delete(
            @retrofit2.http.Path("id") String id            
  );

  /**
   * 
   * 
   * @return Call&lt;List&lt;DrumMap&gt;&gt;
   */
  @GET("api/drum-maps/all/by-author")
  Observable<List<DrumMap>> getAll();
    

  /**
   * 
   * 
   * @param id  (required)
   * @return Call&lt;Fingering&gt;
   */
  @GET("api/drum-maps/by-id/{id}")
  Observable<Fingering> getById(
            @retrofit2.http.Path("id") String id            
  );

  /**
   * 
   * 
   * @param id  (required)
   * @param body  (optional)
   * @return Call&lt;Void&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("api/drum-maps/{id}")
  Observable<Void> update(
            @retrofit2.http.Path("id") String id            ,                 @retrofit2.http.Body DrumMapDto body    
  );

}
