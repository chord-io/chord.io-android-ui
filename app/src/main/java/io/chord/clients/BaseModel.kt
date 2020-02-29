package io.chord.clients

import java.io.Serializable
import kotlin.random.Random
import kotlin.reflect.full.memberProperties

abstract class BaseModel : Serializable
{
	private var _referenceId = Random.nextInt()
	
	val referenceId: Int
		get() = this._referenceId
	
	protected fun regenerate()
	{
		this._referenceId = Random.nextInt()
		
		this.javaClass.kotlin.memberProperties.forEach { member ->
			val instance = member.get(this) ?: return@forEach
			
			if(instance is List<*>)
			{
				instance.forEach {
					if(it is BaseModel)
					{
						it.regenerate()
					}
				}
			}
			else if(instance is BaseModel)
			{
				instance.regenerate()
			}
		}
	}
}