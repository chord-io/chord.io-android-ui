package io.chord.client

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.chord.client.models.ValidationProblemDetails
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext

fun Throwable.toApiThrowable(): ApiThrowable = ApiThrowable(this)

class ApiThrowable(
	private val throwable: Throwable
)
{
	private var onError: ((code: Int, message: String) -> Unit)? = null
	private var onValidationError: ((mapper: ValidationProblemDetailMapper) -> Unit)? = null
	private var onConnectionTimeout: (() -> Unit)? = null
	private var onNetworkError: (() -> Unit)? = null
	private var onPostExecute: (() -> Unit)? = null
	
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
	
	fun doOnPostExecute(callback: (() -> Unit)): ApiThrowable
	{
		this.onPostExecute = callback
		return this
	}
	
	fun execute()
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
					
					runBlocking(Dispatchers.Main) {
						onValidationError?.invoke(
							ValidationProblemDetailMapper(validationProblemDetails)
						)
					}
				}
				else
				{
					runBlocking(Dispatchers.Main)
					{
						onError?.invoke(code, message)
					}
				}
			}
			is SocketTimeoutException ->
			{
				runBlocking(Dispatchers.Main)
				{
					onConnectionTimeout?.invoke()
				}
			}
			is IOException ->
			{
				runBlocking(Dispatchers.Main)
				{
					onNetworkError?.invoke()
				}
			}
		}
		
		runBlocking(Dispatchers.Main)
		{
			onPostExecute?.invoke()
		}
	}
}