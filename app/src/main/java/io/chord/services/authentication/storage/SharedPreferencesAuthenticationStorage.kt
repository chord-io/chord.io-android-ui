package io.chord.services.authentication.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import io.chord.R
import io.chord.services.authentication.models.AuthenticationRepresentation
import java.util.*

open class SharedPreferencesAuthenticationStorage(
	private val preferences: SharedPreferences,
	private val gson: Gson
) : AuthenticationStorage
{
	override fun store(authentication: AuthenticationRepresentation)
	{
		this.preferences
			.edit()
			.putString(R.string.auth_storage_key.toString(), gson.toJson(authentication))
			.apply()
	}
	
	override fun retrieve(): AuthenticationRepresentation?
	{
		val authentication = this.preferences.getString(
			R.string.auth_storage_key.toString(),
			null
		)
		return if (authentication == null)
		{
			null
		}
		else
		{
			gson.fromJson(authentication, AuthenticationRepresentation::class.java)
		}
	}
	
	override fun contains(): Boolean
	{
		return this.preferences.contains(R.string.auth_storage_key.toString())
	}
	
	override fun remove()
	{
		this.preferences
			.edit()
			.remove(R.string.auth_storage_key.toString())
			.apply()
	}
	
	override fun isExpired(): Boolean
	{
		return Calendar.getInstance().after(this.retrieve()!!.expirationDate)
	}
	
	override fun isRefreshExpired(): Boolean
	{
		return Calendar.getInstance().after(this.retrieve()!!.refreshExpirationDate)
	}
	
	override fun isAuthenticated(): Boolean
	{
		if(!this.contains())
		{
			return false
		}
		
		return !this.isExpired()
	}
}