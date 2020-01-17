package io.chord.ui.utils

import kotlin.math.*
import kotlin.streams.toList

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
		
		fun step(value: Float, steps: List<Float>): Float
		{
			val first = steps.first()
			val last = steps.last()
			
			return when
			{
				value < first -> first
				value > last -> last
				else -> steps
					.stream()
					.filter { it >= value }
					.min { a, b -> a.compareTo(b) }
					.get()
			}
			
		}
		
		
		fun nearest(value: Float, values: List<Float>): Float
		{
			val first = values.first()
			val last = values.last()
			
			return when
			{
				value < first -> first
				value > last -> last
				else -> values
					.stream()
					.map {
						val min = min(it, value)
						val max = max(it, value)
						Pair<Float, Float>(max - min, it)
					}
					.min { a, b -> a.first.compareTo(b.first) }
					.get()
					.second
			}
			
		}
	}
}