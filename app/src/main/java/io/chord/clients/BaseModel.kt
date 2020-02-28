package io.chord.clients

import java.io.Serializable
import kotlin.random.Random

open class BaseModel : Serializable
{
	private var _referenceId = Random.nextInt()
	
	val referenceId: Int
		get() = this._referenceId
}