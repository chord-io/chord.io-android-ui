package io.chord.clients.apis;

import io.reactivex.Observable;
import io.reactivex.Completable;
import retrofit2.http.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.MultipartBody;

import io.chord.clients.models.NoteDto;
import io.chord.clients.models.ValidationProblemDetails;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

public interface ArithmeticNoteApi
{
    /**
    * @param semitones  (required)
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/alter/downward/{semitones}")
    Observable<NoteDto> alterDownward(
        @retrofit2.http.Path("semitones") Integer semitones,
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param semitones  (required)
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/alter/upward/{semitones}")
    Observable<NoteDto> alterUpward(
        @retrofit2.http.Path("semitones") Integer semitones,
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note")
    Observable<NoteDto> create(
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param index  (required)
    * @return Observable&lt;NoteDto&gt;
    */
    @POST("api/arithmetic/note/from-integral/{index}")
    Observable<NoteDto> fromIntegral(
        @retrofit2.http.Path("index") Integer index
    );

    /**
    * @param index  (required)
    * @return Observable&lt;NoteDto&gt;
    */
    @POST("api/arithmetic/note/from-midi/{index}")
    Observable<NoteDto> fromMidi(
        @retrofit2.http.Path("index") Integer index
    );

    /**
    * @param note  (required)
    * @return Observable&lt;NoteDto&gt;
    */
    @POST("api/arithmetic/note/from-string/{note}")
    Observable<NoteDto> fromString(
        @retrofit2.http.Path("note") String note
    );

    /**
    * @param degree  (required)
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/interval/{degree}")
    Observable<NoteDto> getInterval(
        @retrofit2.http.Path("degree") Integer degree,
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param distance  (required)
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/shift/to-left/{distance}")
    Observable<NoteDto> shiftToLeft(
        @retrofit2.http.Path("distance") Integer distance,
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param distance  (required)
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/shift/to-right/{distance}")
    Observable<NoteDto> shiftToRight(
        @retrofit2.http.Path("distance") Integer distance,
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/simplify")
    Observable<NoteDto> simplify(
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;Integer&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/to-integral")
    Observable<Integer> toIntegral(
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;Integer&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/to-midi")
    Observable<Integer> toMidi(
        @retrofit2.http.Body NoteDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;String&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/note/to-string")
    Observable<String> toString(
        @retrofit2.http.Body NoteDto body
    );
}
