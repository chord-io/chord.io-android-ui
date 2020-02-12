package io.chord.clients.apis;

import java.util.UUID;

import io.chord.clients.models.UserDto;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface UsersApi {
  /**
   * 
   * 
   * @param id  (required)
   * @return Call&lt;Void&gt;
   */
  @DELETE("api/users/{id}")
  Observable<Void> delete(
            @retrofit2.http.Path("id") UUID id            
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
  @PUT("api/users/{id}")
  Observable<Void> update(
            @retrofit2.http.Path("id") UUID id            ,                 @retrofit2.http.Body UserDto body    
  );

}
