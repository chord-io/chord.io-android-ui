/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.client.apis

import okhttp3.RequestBody

import io.chord.client.models.NoteDto
import io.chord.client.models.ValidationProblemDetails
import io.reactivex.Single
import retrofit2.http.Headers
import retrofit2.http.POST

@JvmSuppressWildcards
interface ArithmeticNoteApi {
  /**
   * The endpoint is owned by chord.io.api service owner
   * @param semitones  (required)
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: AlterDownward",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/alter/downward/{semitones}")
  fun alterDownward(
    @retrofit2.http.Path("semitones") semitones: Int,
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param semitones  (required)
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: AlterUpward",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/alter/upward/{semitones}")
  fun alterUpward(
    @retrofit2.http.Path("semitones") semitones: Int,
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: Create",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note")
  fun create(
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param index  (required)
   */
  @Headers(
    "X-Operation-Id: FromIntegral"
  )
  @POST("/api/arithmetic/note/from-integral/{index}")
  fun fromIntegral(
    @retrofit2.http.Path("index") index: Int
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param index  (required)
   */
  @Headers(
    "X-Operation-Id: FromMidi"
  )
  @POST("/api/arithmetic/note/from-midi/{index}")
  fun fromMidi(
    @retrofit2.http.Path("index") index: Int
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param note  (required)
   */
  @Headers(
    "X-Operation-Id: FromString"
  )
  @POST("/api/arithmetic/note/from-string/{note}")
  fun fromString(
    @retrofit2.http.Path("note") note: String
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param degree  (required)
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: GetInterval",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/interval/{degree}")
  fun getInterval(
    @retrofit2.http.Path("degree") degree: Int,
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param distance  (required)
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: ShiftToLeft",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/shift/to-left/{distance}")
  fun shiftToLeft(
    @retrofit2.http.Path("distance") distance: Int,
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param distance  (required)
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: ShiftToRight",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/shift/to-right/{distance}")
  fun shiftToRight(
    @retrofit2.http.Path("distance") distance: Int,
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: Simplify",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/simplify")
  fun simplify(
    @retrofit2.http.Body  body: NoteDto
  ): Single<NoteDto>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: ToIntegral",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/to-integral")
  fun toIntegral(
    @retrofit2.http.Body  body: NoteDto
  ): Single<Int>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: ToMidi",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/to-midi")
  fun toMidi(
    @retrofit2.http.Body  body: NoteDto
  ): Single<Int>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: ToString",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/arithmetic/note/to-string")
  fun toString(
    @retrofit2.http.Body  body: NoteDto
  ): Single<String>

}