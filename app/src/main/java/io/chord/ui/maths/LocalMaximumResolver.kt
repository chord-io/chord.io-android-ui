package io.chord.ui.maths

import io.chord.ui.utils.ViewUtils
import kotlin.math.abs
import kotlin.math.sign

class LocalMaximumResolver(
	private var value: Float,
	private val tolerance: Float
)
{
	private val values: MutableList<Float> = mutableListOf()
	
	fun resolve(evaluator: ((Float) -> Float)): Float
	{
		this.values.clear()
		this.values.add(this.value)
		var lastValue: Float
		
		do
		{
			lastValue = this.values.last()
			this.value = evaluator.invoke(lastValue)
		}
		while(!this.isOptimal(this.value, lastValue))
		
		return this.value
	}
	
	private fun isOptimal(value: Float, lastValue: Float): Boolean
	{
		val isOptimal = abs(value - lastValue) < this.tolerance
		
		if(isOptimal)
		{
			return true
		}
		
		this.values.add(value)
		
		return false
	}
	
}