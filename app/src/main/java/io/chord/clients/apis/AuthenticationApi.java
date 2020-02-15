package io.chord.clients.apis;

import io.reactivex.Observable;
import io.reactivex.Completable;
import retrofit2.http.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import io.chord.clients.models.Authentication;
import io.chord.clients.models.SignInDto;
import io.chord.clients.models.User;
import io.chord.clients.models.UserDto;
import io.chord.clients.models.ValidationProblemDetails;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

public interface AuthenticationApi
{
    /**
    * @param refreshToken  (optional)
    * @return Observable&lt;Authentication&gt;
    */
    @GET("api/authentication/refresh")
    Observable<Authentication> refresh(
        @retrofit2.http.Header("refresh_token") String refreshToken
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;Authentication&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/authentication/sign-in")
    Observable<Authentication> signIn(
        @retrofit2.http.Body SignInDto body
    );

    /**
    * @param refreshToken  (optional)
    * @return Completable
    */
    @GET("api/authentication/sign-out")
    Completable signOut(
        @retrofit2.http.Header("refresh_token") String refreshToken
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;User&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/authentication/sign-up")
    Observable<User> signUp(
        @retrofit2.http.Body UserDto body
    );
}
