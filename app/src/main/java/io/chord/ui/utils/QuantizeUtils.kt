package io.chord.ui.utils

import kotlin.math.ceil

class QuantizeUtils
{
	enum class QuantizeMode
	{
		Natural,
		Ternary,
		Dotted
	}
	
	enum class QuantizeValue(val value: Int)
	{
		First(1),
		Second(2),
		Fourth(4),
		Eighth(8),
		Sixteenth(16),
		ThirtySecond(32),
		SixtyFourth(64),
		HundredTwentyEighth(128)
	}
	
	class Quantization(
		value: QuantizeValue,
		val mode: QuantizeMode
	)
	{
		val value: Float = quantify(value, mode)
		val count: Int = count(value, mode)
	}
	
	companion object
	{
		val values: List<Pair<QuantizeMode, QuantizeValue>> = listOf(
			Pair(QuantizeMode.Natural, QuantizeValue.First),
			Pair(QuantizeMode.Natural, QuantizeValue.Second),
			Pair(QuantizeMode.Natural, QuantizeValue.Fourth),
			Pair(QuantizeMode.Natural, QuantizeValue.Eighth),
			Pair(QuantizeMode.Natural, QuantizeValue.Sixteenth),
			Pair(QuantizeMode.Natural, QuantizeValue.ThirtySecond),
			Pair(QuantizeMode.Natural, QuantizeValue.SixtyFourth),
			Pair(QuantizeMode.Natural, QuantizeValue.HundredTwentyEighth),
			
			Pair(QuantizeMode.Ternary, QuantizeValue.Second),
			Pair(QuantizeMode.Ternary, QuantizeValue.Fourth),
			Pair(QuantizeMode.Ternary, QuantizeValue.Eighth),
			Pair(QuantizeMode.Ternary, QuantizeValue.Sixteenth),
			Pair(QuantizeMode.Ternary, QuantizeValue.ThirtySecond),
			Pair(QuantizeMode.Ternary, QuantizeValue.SixtyFourth),
			Pair(QuantizeMode.Ternary, QuantizeValue.HundredTwentyEighth),
			
			Pair(QuantizeMode.Dotted, QuantizeValue.Second),
			Pair(QuantizeMode.Dotted, QuantizeValue.Fourth),
			Pair(QuantizeMode.Dotted, QuantizeValue.Eighth),
			Pair(QuantizeMode.Dotted, QuantizeValue.Sixteenth),
			Pair(QuantizeMode.Dotted, QuantizeValue.ThirtySecond),
			Pair(QuantizeMode.Dotted, QuantizeValue.SixtyFourth),
			Pair(QuantizeMode.Dotted, QuantizeValue.HundredTwentyEighth)
		)
		
		fun quantify(value: QuantizeValue, mode: QuantizeMode): Float
		{
			if(value == QuantizeValue.First && (mode == QuantizeMode.Ternary || mode == QuantizeMode.Dotted))
			{
				throw IllegalStateException("value parameter must be greater than First when mode is set to ternary or dotted")
			}
			
			val n = value.value.toFloat()
			
			return when(mode)
			{
				QuantizeMode.Natural -> 1 / n
				QuantizeMode.Ternary -> 1/ count(value, mode).toFloat()
				QuantizeMode.Dotted -> 0.75f / (n / 2)
			}
		}
		
		fun count(value: QuantizeValue, mode: QuantizeMode): Int
		{
			val n = value.value
			
			return when(mode)
			{
				QuantizeMode.Natural -> n
				QuantizeMode.Ternary -> n / 2 * 3
				QuantizeMode.Dotted -> ceil(1 / (0.75f / (n / 2))).toInt() + 1
			}
		}
	}
}