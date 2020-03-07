package io.chord.ui.extensions

import kotlin.math.floor
import kotlin.math.roundToInt

fun Float.roundToOddInt(): Int
{
	val previous = floor(this).toInt()
	val value = this.roundToInt()
	val isOdd = value % 2 == 1
	
	return when
	{
		isOdd -> value
		value == previous -> value - 1
		else -> value + 1
	}
}