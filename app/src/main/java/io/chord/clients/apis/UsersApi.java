package io.chord.clients.apis;

import io.reactivex.Observable;
import io.reactivex.Completable;
import retrofit2.http.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import java.util.UUID;
import io.chord.clients.models.UserDto;
import io.chord.clients.models.ValidationProblemDetails;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

public interface UsersApi
{
    /**
    * @param id  (required)
    * @return Completable
    */
    @DELETE("api/users/{id}")
    Completable delete(
        @retrofit2.http.Path("id") UUID id
    );

    /**
    * @param id  (required)
    * @param body  (optional)
    * @return Completable
    */
    @Headers({"Content-Type:application/json"})
    @PUT("api/users/{id}")
    Completable update(
        @retrofit2.http.Path("id") UUID id,
        @retrofit2.http.Body UserDto body
    );
}
