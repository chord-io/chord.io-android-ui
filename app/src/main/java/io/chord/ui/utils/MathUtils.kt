package io.chord.ui.utils

import kotlin.math.*

class MathUtils
{
	companion object
	{
		fun map(value: Float, a: Float, b: Float, c: Float, d: Float): Float
		{
			return (value - a) / (b - a) * (d-c) + c
		}
		
		fun map(value: Int, a: Int, b: Int, c: Int, d: Int): Int
		{
			return map(
				value.toFloat(),
				a.toFloat(),
				b.toFloat(),
				c.toFloat(),
				d.toFloat()
			).toInt()
		}
		
		fun linearInterpolation(time: Float, start: Float, end:Float): Float
		{
			return start + time * (end - start)
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
				value <= first -> first
				value >= last -> last
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
				value <= first -> first
				value >= last -> last
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
		
		fun ratio(a: Float, b: Float): Float
		{
			val min = min(a, b)
			val max = max(a, b)
			return min / max
		}
		
		fun ratio(a: Int, b: Int): Float
		{
			return ratio(a.toFloat(), b.toFloat())
		}
	}
}