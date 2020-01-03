package io.chord.client

import io.chord.client.models.ValidationProblemDetails
import java.util.function.BiConsumer

class ValidationProblemDetailMapper(
	private val validationProblemDetails: ValidationProblemDetails
)
{
	private val map: MutableMap<String, ((String) -> Unit)> = mutableMapOf()
	
	fun map(key: String, callback: ((String) -> Unit)): ValidationProblemDetailMapper
	{
		this.map[key] = callback
		return this
	}
	
	fun execute()
	{
		val errors = this.validationProblemDetails.errors!!
		
		this.map.forEach { (key, callback) ->
			if(errors.containsKey(key))
			{
				callback(errors.getValue(key).first())
			}
		}
	}
}