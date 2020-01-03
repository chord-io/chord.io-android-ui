package io.chord.services.authentication.models

import com.squareup.moshi.Json
import java.util.*

data class AuthenticationRepresentation
(
	@Json(name = "access_token") @field:Json(name = "access_token") var accessToken: String,
	@Json(name = "expires_in") @field:Json(name = "expires_in") var expiresIn: Int,
	@Json(name = "refresh_expires_in") @field:Json(name = "refresh_expires_in") var refreshExpiresIn: Int,
	@Json(name = "refreshToken") @field:Json(name = "refreshToken") var refreshToken: String,
	@Json(name = "token_type") @field:Json(name = "token_type") var tokenType: String,
	@Json(name = "not-before-policy") @field:Json(name = "not-before-policy") var notBeforePolicy: Int,
	@Json(name = "session_state") @field:Json(name = "session_state") var sessionState: String,
	@Json(name = "scope") @field:Json(name = "scope") var scope: String,
	var expirationDate: Calendar? = null,
	var refreshExpirationDate: Calendar? = null
)