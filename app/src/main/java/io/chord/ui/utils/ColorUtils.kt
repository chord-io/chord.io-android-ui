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
	}
}