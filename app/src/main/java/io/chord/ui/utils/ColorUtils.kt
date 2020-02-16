package io.chord.ui.utils

import android.graphics.Color
import kotlin.math.max
import kotlin.math.min

class ColorUtils
{
	companion object
	{
		fun contrast(a: Int, b: Int): Float
		{
			val aLuminance = Color.valueOf(a).luminance()
			val bLuminance = Color.valueOf(b).luminance()
			val brightest = max(aLuminance, bLuminance)
			val darkest = min(aLuminance, bLuminance)
			return (brightest + 0.05f) / (darkest + 0.05f)
		}
		
		fun readableTextColor(backgroundColor: Int, a: Int, b: Int): Int
		{
			val aContrast = ColorUtils.contrast(backgroundColor, a)
			val bContrast = ColorUtils.contrast(backgroundColor, b)
			
			return if(aContrast > bContrast)
			{
				a
			}
			else
			{
				b
			}
		}
	}
}