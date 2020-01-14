package io.chord.clients

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.chord.clients.models.ValidationProblemDetails
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

fun Throwable.toApiThrowable(): ApiThrowable = ApiThrowable(this)

class ApiThrowable(
	private val throwable: Throwable
)
{
	private var onError: ((code: Int, message: String) -> Unit)? = null
	private var onValidationError: ((mapper: ValidationProblemDetailMapper) -> Unit)? = null
	private var onConnectionTimeout: (() -> Unit)? = null
	private var onNetworkError: (() -> Unit)? = null
	private var onPostObservation: (() -> Unit)? = null
	
	fun doOnError(callback: ((code: Int, message: String) -> Unit)): ApiThrowable
	{
		this.onError = callback
		return this
	}
	
	fun doOnValidationError(callback: ((mapper: ValidationProblemDetailMapper) -> Unit)): ApiThrowable
	{
		this.onValidationError = callback
		return this
	}
	
	fun doOnConnectionTimeout(callback: (() -> Unit)): ApiThrowable
	{
		this.onConnectionTimeout = callback
		return this
	}
	
	fun doOnNetworkError(callback: (() -> Unit)): ApiThrowable
	{
		this.onNetworkError = callback
		return this
	}
	
	fun doOnPostObservation(callback: (() -> Unit)): ApiThrowable
	{
		this.onPostObservation = callback
		return this
	}
	
	fun observe()
	{
		when(this.throwable)
		{
			is HttpException ->
			{
				val code = this.throwable.code()
				val message = this.throwable.response()!!.errorBody()!!.string()
				
				if(code == 400)
				{
					val validationProblemDetails = jacksonObjectMapper().readValue(
						message,
						ValidationProblemDetails::class.java
					)
					
					this.onValidationError?.invoke(
						ValidationProblemDetailMapper(validationProblemDetails)
					)
				}
				else
				{
					this.onError?.invoke(code, message)
				}
			}
			is SocketTimeoutException ->
			{
				this.onConnectionTimeout?.invoke()
			}
			is IOException ->
			{
				this.onNetworkError?.invoke()
			}
		}
		
		this.onPostObservation?.invoke()
	}
}