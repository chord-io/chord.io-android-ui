package io.chord.ui.extensions

import android.graphics.Color

fun Int.toTransparent(alpha: Float): Int
{
	val colorObject = Color.valueOf(this)
	
	return Color.argb(
		alpha,
		colorObject.red(),
		colorObject.green(),
		colorObject.blue()
	)
}

fun Int.toHsv(): FloatArray
{
	val hsv = floatArrayOf(0f, 0f, 0f)
	Color.colorToHSV(this, hsv)
	return hsv
}

fun FloatArray.fromHsv(): Int
{
	return Color.HSVToColor(this)
}