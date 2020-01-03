package io.chord.services.authentication.api

import android.annotation.SuppressLint
import io.chord.R
import io.chord.client.tools.GeneratedCodeConverters
import io.chord.services.authentication.models.AuthenticationRepresentation
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.services.authentication.workers.RefreshTokenWorker
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Response
import org.keycloak.representations.account.UserRepresentation
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.*

class KeycloakClient : KoinComponent
{
	private val storage: SharedPreferencesAuthenticationStorage by inject()
	private var api: KeycloakApi
	
	init
	{
		val client = OkHttpClient.Builder()
			.addInterceptor(BearerAuthorizationInterceptor())
			.build()
		this.api = Retrofit.Builder()
			.client(client)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.addConverterFactory(GeneratedCodeConverters.converterFactory())
			.baseUrl(R.string.auth_base_url.toString())
			.build()
			.create(KeycloakApi::class.java)
	}
	
	private fun postAuthentication(authentication: AuthenticationRepresentation)
	{
		val expirationDate = Calendar.getInstance().clone() as Calendar
		val refreshExpirationDate = Calendar.getInstance().clone() as Calendar
		expirationDate.add(Calendar.SECOND, authentication.expiresIn)
		refreshExpirationDate.add(Calendar.SECOND, authentication.refreshExpiresIn)
		authentication.expirationDate = expirationDate
		authentication.refreshExpirationDate = refreshExpirationDate
	}
	
	@SuppressLint("CheckResult")
	suspend fun authenticate(
		username: String,
		password: String
	)
	{
		val task = this.api.authenticate(
			R.string.auth_realm.toString(),
			R.string.auth_client_id.toString(),
			R.string.auth_client_secret.toString(),
			username,
			password
		)
		
		task.subscribe(
			{ authentication ->
				run {
					this.postAuthentication(authentication)
					this.storage.store(authentication)
					RefreshTokenWorker.start(R.integer.refresh_token_worker_interval.toLong())
				}
			},
			{ error -> throw error }
		)
	}
	
	@SuppressLint("CheckResult")
	suspend fun refresh()
	{
		val task = this.api.refresh(
			R.string.auth_realm.toString(),
			R.string.auth_client_id.toString(),
			R.string.auth_client_secret.toString(),
			this.storage.retrieve()!!.refreshToken
		)
		
		task.subscribe(
			{ authentication ->
				run {
					this.postAuthentication(authentication)
					this.storage.store(authentication)
				}
			},
			{ error -> throw error }
		)
	}
	
	@SuppressLint("CheckResult")
	suspend fun logout()
	{
		val task = this.api.logout(
			this.storage.retrieve()!!.accessToken,
			R.string.auth_realm.toString(),
			R.string.auth_client_id.toString(),
			R.string.auth_client_secret.toString(),
			this.storage.retrieve()!!.refreshToken
		)
		
		task.subscribe(
			{
				run {
					this.storage.remove()
					RefreshTokenWorker.cancel()
				}
			},
			{ error -> throw error }
		)
	}
	
	suspend fun getUser(id: String): Single<UserRepresentation>
	{
		return this.api.getUser(
			this.storage.retrieve()!!.accessToken,
			R.string.auth_realm.toString(),
			id
		)
	}
	
	suspend fun getUserByEmail(email: String): Single<List<UserRepresentation>>
	{
		return this.api.getUserByEmail(
			this.storage.retrieve()!!.accessToken,
			R.string.auth_realm.toString(),
			email
		)
	}
	
	suspend fun createUser(user: UserRepresentation): Single<Response>
	{
		return this.api.createUser(
			this.storage.retrieve()!!.accessToken,
			R.string.auth_realm.toString(),
			user
		)
	}
	
	suspend fun updateUser(id: String, user: UserRepresentation): Single<Response>
	{
		return this.api.updateUser(
			this.storage.retrieve()!!.accessToken,
			R.string.auth_realm.toString(),
			id,
			user
		)
	}

	suspend fun deleteUser(id: String): Single<Response>
	{
		return this.api.deleteUser(
			this.storage.retrieve()!!.accessToken,
			R.string.auth_realm.toString(),
			id
		)
	}
}