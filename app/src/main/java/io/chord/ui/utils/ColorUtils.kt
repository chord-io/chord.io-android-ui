package io.chord.ui.utils

import android.graphics.Color

class ColorUtils
{
	// TODO utils class as extension class
	companion object
	{
		fun toTransparent(color: Int, alpha: Float): Int
		{
			val colorObject = Color.valueOf(color)
			
			return Color.argb(
				alpha,
				colorObject.red(),
				colorObject.green(),
				colorObject.blue()
			)
		}
		
		fun toHsv(color: Int): FloatArray
		{
			val hsv = floatArrayOf(0f, 0f, 0f)
			Color.colorToHSV(color, hsv)
			return hsv
		}
		
		fun fromHsv(color: FloatArray): Int
		{
			return Color.HSVToColor(color)
		}
	}
}