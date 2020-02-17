package io.chord.ui.utils

import android.graphics.Color
import io.chord.ui.extensions.fromHsl
import io.chord.ui.extensions.toHsl
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
		
		fun readableColor(backgroundColor: Int, a: Int, b: Int): Int
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
		
		fun findOptimalReadableColor(
			backgroundColor: Int,
			foregroundColor: Int,
			minimalRatio: Float = 4.5f,
			threshold: Int = 1
		): Int
		{
			val model = foregroundColor.toHsl()

			for(x in threshold..256)
			{
				model[2] = MathUtils.map(
					x.toFloat(),
					1f,
					256f,
					0f,
					1f
				)
				val candidate = model.fromHsl()
				val contrast = ColorUtils.contrast(backgroundColor, candidate)
				
				if(contrast > minimalRatio)
				{
					return candidate
				}
			}
			
			return foregroundColor
		}
	}
}