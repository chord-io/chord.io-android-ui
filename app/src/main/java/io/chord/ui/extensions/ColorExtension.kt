package io.chord.ui.extensions

import android.graphics.Color
import java.util.Collections.max
import java.util.Collections.min
import kotlin.math.min


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

fun Int.toHsl(): FloatArray
{
	val color = Color.valueOf(this)
	val r = color.red()
	val g = color.green()
	val b = color.blue()
	val components = listOf(r, g, b)
	
	val minimum = min(components)
	val maximum = max(components)
	
	var h: Float
	val s: Float
	val l: Float
	
	l = (maximum + minimum) / 2.0f
	
	if(maximum == minimum)
	{
		s = 0.0f
		h = s
	}
	else
	{
		val d: Float = maximum - minimum
		s = if(l > 0.5f) d / (2.0f - maximum - minimum) else d / (maximum + minimum)
		h = if(r > g && r > b) (g - b) / d + (if(g < b) 6.0f else 0.0f)
		else if(g > b) (b - r) / d + 2.0f
		else (r - g) / d + 4.0f
		h /= 6.0f
	}
	
	return floatArrayOf(h, s, l)
}

fun FloatArray.fromHsl(): Int
{
	fun to255(value: Float): Int
	{
		return min(255f, 256f * value).toInt()
	}
	
	fun hueToRgb(p: Float, q: Float, t: Float): Float
	{
		var tt = t
		if (tt < 0f)
			tt += 1f
		if (tt > 1f)
			tt -= 1f
		if (tt < 1f/6f)
			return p + (q - p) * 6f * tt
		if (tt < 1f/2f)
			return q
		if (tt < 2f/3f)
			return p + (q - p) * (2f/3f - tt) * 6f
		return p
	}
	
	val h = this[0]
	val s = this[1]
	val l = this[2]
	val r: Float
	val g: Float
	val b: Float
	
	if(s == 0f)
	{
		b = l
		g = b
		r = g
	}
	else
	{
		val q = if(l < 0.5f) l * (1 + s) else l + s - l * s
		val p = 2 * l - q
		r = hueToRgb(p, q, h + 1f / 3f)
		g = hueToRgb(p, q, h)
		b = hueToRgb(p, q, h - 1f / 3f)
	}
	
	return Color.valueOf(r, g, b).toArgb()
}