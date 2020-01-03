package io.chord.services.authentication.api

import io.chord.services.authentication.models.AuthenticationRepresentation
import io.reactivex.Single
import okhttp3.Response
import org.keycloak.representations.account.UserRepresentation
import retrofit2.http.*

interface KeycloakApi
{
	@Headers(
		"Content-Type: application/x-www-form-urlencoded"
	)
	@POST("/realms/{realm}/protocol/openid-connect/token")
	@FormUrlEncoded
	suspend fun authenticate(
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Field("client_id") clientId: String,
		@retrofit2.http.Field("client_secret") clientSecret: String,
		@retrofit2.http.Field("username") username: String,
		@retrofit2.http.Field("password") password: String,
		@retrofit2.http.Field("grant_type") grantType: String = "password"
	): Single<AuthenticationRepresentation>
	
	@Headers(
		"Content-Type: application/x-www-form-urlencoded"
	)
	@POST("/realms/{realm}/protocol/openid-connect/token")
	@FormUrlEncoded
	suspend fun refresh(
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Field("client_id") clientId: String,
		@retrofit2.http.Field("client_secret") clientSecret: String,
		@retrofit2.http.Field("refresh_token") refreshToken: String,
		@retrofit2.http.Field("grant_type") grantType: String = "refresh_token"
	): Single<AuthenticationRepresentation>
	
	@Headers(
		"Content-Type: application/x-www-form-urlencoded"
	)
	@POST("/realms/{realm}/protocol/openid-connect/logout")
	@FormUrlEncoded
	suspend fun logout(
		@retrofit2.http.Header("Authorization") accessToken: String,
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Field("client_id") clientId: String,
		@retrofit2.http.Field("client_secret") clientSecret: String,
		@retrofit2.http.Field("refresh_token") refresh_token: String
	): Single<Response>
	
	@Headers(
		"Content-Type: application/json"
	)
	@GET("/{realm}/users/{id}")
	suspend fun getUser(
		@retrofit2.http.Header("Authorization") accessToken: String,
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Path("id") id: String
	): Single<UserRepresentation>
	
	@Headers(
		"Content-Type: application/json"
	)
	@GET("/{realm}/users/{email}")
	suspend fun getUserByEmail(
		@retrofit2.http.Header("Authorization") accessToken: String,
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Query("email") email: String
	): Single<List<UserRepresentation>>
	
	@Headers(
		"Content-Type: application/json"
	)
	@POST("/{realm}/users")
	suspend fun createUser(
		@retrofit2.http.Header("Authorization") accessToken: String,
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Body rep: UserRepresentation
	): Single<Response>
	
	@Headers(
		"Content-Type: application/json"
	)
	@PUT("/{realm}/users/{id}")
	suspend fun updateUser(
		@retrofit2.http.Header("Authorization") accessToken: String,
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Path("id") id: String,
		@retrofit2.http.Body rep: UserRepresentation
	): Single<Response>
	
	@Headers(
		"Content-Type: application/json"
	)
	@DELETE("/{realm}/users/{id}")
	suspend fun deleteUser(
		@retrofit2.http.Header("Authorization") accessToken: String,
		@retrofit2.http.Path("realm") realm: String,
		@retrofit2.http.Path("id") id: String
	): Single<Response>
}