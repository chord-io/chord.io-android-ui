package io.chord.services.authentication.storage

import io.chord.clients.models.Authentication

interface AuthenticationStorage
{
	fun store(authentication: Authentication)
	
	fun retrieve(): Authentication?
	
	fun contains(): Boolean
	
	fun remove()
	
	fun isExpired(): Boolean
	
	fun isRefreshExpired(): Boolean
	
	fun isAuthenticated(): Boolean
	
	fun getUserId(): String?
	
	fun getUsername(): String?
}