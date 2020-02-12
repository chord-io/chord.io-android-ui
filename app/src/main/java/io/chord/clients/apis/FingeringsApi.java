package io.chord.clients.apis;

import java.util.List;

import io.chord.clients.models.Fingering;
import io.chord.clients.models.FingeringDto;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface FingeringsApi {
  /**
   * 
   * 
   * @param body  (optional)
   * @return Call&lt;Fingering&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/fingerings")
  Observable<Fingering> create(
                    @retrofit2.http.Body FingeringDto body    
  );

  /**
   * 
   * 
   * @param id  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("api/fingerings/{id}")
  Observable<Void> delete(
            @retrofit2.http.Path("id") String id            
  );

  /**
   * 
   * 
   * @return Call&lt;List&lt;Fingering&gt;&gt;
   */
  @GET("api/fingerings/all/by-author")
  Observable<List<Fingering>> getAllByAuthor();
    

  /**
   * 
   * 
   * @param id  (required)
   * @return Call&lt;Fingering&gt;
   */
  @GET("api/fingerings/by-id/{id}")
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
  @PUT("api/fingerings/{id}")
  Observable<Void> update(
            @retrofit2.http.Path("id") String id            ,                 @retrofit2.http.Body FingeringDto body    
  );

}
