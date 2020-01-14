package io.chord.clients

import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.IllegalStateException

class AuthorizeClientInterceptor : Interceptor, KoinComponent
{
	private val storage: SharedPreferencesAuthenticationStorage by inject()
	
	override fun intercept(chain: Interceptor.Chain): Response
	{
		try
		{
			val token = this.storage.retrieve()!!.accessToken
			var request = chain.request()
			
			request = request.newBuilder()
				.addHeader("Authorization", "Bearer $token")
				.build()
			
			return chain.proceed(request)
		}
		catch(exception: KotlinNullPointerException)
		{
			throw IllegalStateException("to authorize an http client, a valid authentication must be performed")
		}
	}
}