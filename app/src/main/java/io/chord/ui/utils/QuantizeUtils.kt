package io.chord.ui.utils

class QuantizeUtils
{
	enum class QuantizeMode(val mode: Int)
	{
		Natural(0),
		Ternary(1),
		Dotted(2)
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
		
		fun quantize(value: QuantizeValue, mode: QuantizeMode): Float
		{
			if(value == QuantizeValue.First && (mode == QuantizeMode.Ternary || mode == QuantizeMode.Dotted))
			{
				throw IllegalStateException("value parameter must be greater than First when mode is set to ternary or dotted")
			}
			
			val n = value.value.toFloat()
			
			return when(mode)
			{
				QuantizeMode.Natural -> 1 / n
				QuantizeMode.Ternary -> 1/ ((n - 1) * 3)
				QuantizeMode.Dotted -> 1 / n + ((1 / n) / 2)
			}
		}
	}
}