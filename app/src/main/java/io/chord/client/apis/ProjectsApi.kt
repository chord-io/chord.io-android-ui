/**
 * NOTE: This class is auto generated by the Swagger Gradle Codegen for the following API: chord.io
 *
 * More info on this tool is available on https://github.com/Yelp/swagger-gradle-codegen
 */

package io.chord.client.apis

import okhttp3.RequestBody

import io.chord.client.models.Project
import io.chord.client.models.ProjectDto
import io.chord.client.models.ValidationProblemDetails
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

@JvmSuppressWildcards
interface ProjectsApi {
  /**
   * The endpoint is owned by chord.io.api service owner
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: Create",
    "Content-Type: application/json-patch+json"
  )
  @POST("/api/projects")
  fun create(
    @retrofit2.http.Body  body: ProjectDto
  ): Single<Project>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param id  (required)
   */
  @Headers(
    "X-Operation-Id: Delete"
  )
  @DELETE("/api/projects/{id}")
  fun delete(
    @retrofit2.http.Path("id") id: String
  ): Completable

  /**
   * The endpoint is owned by chord.io.api service owner
   */
  @Headers(
    "X-Operation-Id: GetAllByAuthor"
  )
  @GET("/api/projects/all/by-author")
  fun getAllByAuthor()
    : Single<List<Project>>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param id  (required)
   */
  @Headers(
    "X-Operation-Id: GetById"
  )
  @GET("/api/projects/by-id/{id}")
  fun getById(
    @retrofit2.http.Path("id") id: String
  ): Single<Project>

  /**
   * The endpoint is owned by chord.io.api service owner
   * @param id  (required)
   * @param body  (optional)
   */
  @Headers(
    "X-Operation-Id: Update",
    "Content-Type: application/json-patch+json"
  )
  @PUT("/api/projects/{id}")
  fun update(
    @retrofit2.http.Path("id") id: String,
    @retrofit2.http.Body  body: ProjectDto
  ): Completable

}
