package io.chord.clients.apis;

import io.chord.clients.models.Authentication;
import io.chord.clients.models.SignInDto;
import io.chord.clients.models.User;
import io.chord.clients.models.UserDto;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthenticationApi {
  /**
   * 
   * 
   * @param refreshToken  (optional)
   * @return Call&lt;Authentication&gt;
   */
  @GET("api/authentication/refresh")
  Observable<Authentication> refresh(
                @retrofit2.http.Header("refresh_token") String refreshToken        
  );

  /**
   * 
   * 
   * @param body  (optional)
   * @return Call&lt;Authentication&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/authentication/sign-in")
  Observable<Authentication> signIn(
                    @retrofit2.http.Body SignInDto body    
  );

  /**
   * 
   * 
   * @param refreshToken  (optional)
   * @return Call&lt;Void&gt;
   */
  @GET("api/authentication/sign-out")
  Observable<Void> signOut(
                @retrofit2.http.Header("refresh_token") String refreshToken        
  );

  /**
   * 
   * 
   * @param body  (optional)
   * @return Call&lt;User&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("api/authentication/sign-up")
  Observable<User> signUp(
                    @retrofit2.http.Body UserDto body    
  );

}
