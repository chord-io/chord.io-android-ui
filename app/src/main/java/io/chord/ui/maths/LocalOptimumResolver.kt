package io.chord.ui.maths

import io.chord.ui.utils.ViewUtils
import kotlin.math.abs
import kotlin.math.sign

class LocalOptimumResolver(
	private var value: Float,
	private val tolerance: Float
)
{
	private val values: MutableList<Float> = mutableListOf()
	
	fun resolve(evaluator: ((Float) -> Float)): Float
	{
		this.values.clear()
		this.values.add(this.value)
		
		do
		{
			val lastValue = this.values.last()
			this.value = evaluator.invoke(lastValue)
			
		}
		while(!this.isOptimal(this.value))
		
		return this.value
	}
	
	private fun isOptimal(value: Float): Boolean
	{
		val isOptimal = this.values
			.stream()
			.anyMatch {
				abs(value - it) < this.tolerance
			}
		
		if(isOptimal && this.values.size > 1)
		{
			return true
		}
		
		this.values.add(value)
		
		return false
	}
	
}