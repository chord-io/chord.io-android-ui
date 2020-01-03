package io.chord.services.authentication.storage

import io.chord.services.authentication.models.AuthenticationRepresentation

interface AuthenticationStorage
{
	fun store(authentication: AuthenticationRepresentation)
	
	fun retrieve(): AuthenticationRepresentation?
	
	fun contains(): Boolean
	
	fun remove()
	
	fun isExpired(): Boolean
	
	fun isRefreshExpired(): Boolean
	
	fun isAuthenticated(): Boolean
}