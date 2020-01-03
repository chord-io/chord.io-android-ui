package io.chord.services.authentication.api

import okhttp3.Interceptor
import okhttp3.Response

class BearerAuthorizationInterceptor : Interceptor
{
	override fun intercept(chain: Interceptor.Chain): Response
	{
		var request = chain.request()
		val header = request.header("Authorization")
		
		if(header != null)
		{
			val token = "Bearer $header"
			request = request.newBuilder()
				.addHeader("Authorization", token)
				.build()
		}
		
		return chain.proceed(request)
	}
}