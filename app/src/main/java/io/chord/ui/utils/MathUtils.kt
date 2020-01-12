package io.chord.ui.utils

import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log

class MathUtils
{
	companion object
	{
		fun map(value: Float, a: Float, b: Float, c: Float, d: Float): Float
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
		fun isPowerOf(value: Float, base: Int): Boolean
		{
			return ceil(log(value, base.toFloat())) == floor(log(value, base.toFloat()))
		}
	}
}