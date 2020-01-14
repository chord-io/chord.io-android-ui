package io.chord.clients

import io.chord.clients.models.ValidationProblemDetails

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
	
	fun observe()
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