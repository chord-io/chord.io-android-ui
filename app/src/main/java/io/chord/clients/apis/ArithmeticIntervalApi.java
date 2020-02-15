package io.chord.clients.apis;

import java.util.Map;

import io.chord.clients.models.DegreeAndQualityIntervalDto;
import io.chord.clients.models.DegreeAndSemitonesIntervalDto;
import io.chord.clients.models.IntervalDto;
import io.chord.clients.models.IntervalFormat;
import io.chord.clients.models.IntervalQuality;
import io.chord.clients.models.NoteDto;
import io.chord.clients.models.NotePairDto;
import io.chord.clients.models.SemitonesAndQualityIntervalDto;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ArithmeticIntervalApi
{
    /**
    * @param body  (optional)
    * @return Observable&lt;IntervalDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/from-degree-and-quality")
    Observable<IntervalDto> fromDegreeAndQuality(
        @retrofit2.http.Body DegreeAndQualityIntervalDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;IntervalDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/from-degree-and-semitones")
    Observable<IntervalDto> fromDegreeAndSemitones(
        @retrofit2.http.Body DegreeAndSemitonesIntervalDto body
    );

    /**
    * @param index  (required)
    * @return Observable&lt;IntervalDto&gt;
    */
    @POST("api/arithmetic/interval/from-integral/{index}")
    Observable<IntervalDto> fromIntegral(
        @retrofit2.http.Path("index") Integer index
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;IntervalDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/from-notes")
    Observable<IntervalDto> fromNotes(
        @retrofit2.http.Body NotePairDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;IntervalDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/from-semitones-and-quality")
    Observable<IntervalDto> fromSemitonesAndQuality(
        @retrofit2.http.Body SemitonesAndQualityIntervalDto body
    );

    /**
    * @param interval  (required)
    * @return Observable&lt;IntervalDto&gt;
    */
    @POST("api/arithmetic/interval/from-string/{interval}")
    Observable<IntervalDto> fromString(
        @retrofit2.http.Path("interval") String interval
    );

    /**
    * @return Observable&lt;Map&lt;String, Map&lt;String, IntervalQuality&gt;&gt;&gt;
    */
    @GET("api/arithmetic/interval/look-up-table")
    Observable<Map<String, Map<String, IntervalQuality>>> getLookUpTable();
        

    /**
    * @param body  (optional)
    * @return Observable&lt;IntervalDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/invert")
    Observable<IntervalDto> invert(
        @retrofit2.http.Body IntervalDto body
    );

    /**
    * @param body  (optional)
    * @return Observable&lt;Integer&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/to-integral")
    Observable<Integer> toIntegral(
        @retrofit2.http.Body IntervalDto body
    );

    /**
    * @param root  (required)
    * @param body  (optional)
    * @return Observable&lt;NoteDto&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/to-note/{root}")
    Observable<NoteDto> toNote(
        @retrofit2.http.Path("root") String root,
        @retrofit2.http.Body IntervalDto body
    );

    /**
    * @param format  (required)
    * @param body  (optional)
    * @return Observable&lt;String&gt;
    */
    @Headers({"Content-Type:application/json"})
    @POST("api/arithmetic/interval/to-string/{format}")
    Observable<String> toString(
        @retrofit2.http.Path("format") IntervalFormat format,
        @retrofit2.http.Body IntervalDto body
    );
}
