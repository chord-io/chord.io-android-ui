package io.chord.ui.animations

import android.animation.TypeEvaluator
import io.chord.ui.extensions.fromHsv
import io.chord.ui.extensions.toHsv
import io.chord.ui.utils.MathUtils

class HsvColorEvaluator : TypeEvaluator<Int>
{
	override fun evaluate(
		fraction: Float,
		startValue: Int,
		endValue: Int
	): Int
	{
		val startColor = startValue.toHsv()
		val endColor = endValue.toHsv()
		
		val startH = startColor[0]
		val endH = endColor[0]
		val fractionH = MathUtils.linearInterpolation(fraction, startH, endH)
		
		val startS = startColor[1]
		val endS = endColor[1]
		val fractionS = MathUtils.linearInterpolation(fraction, startS, endS)
		
		val startV = startColor[2]
		val endV = endColor[2]
		val fractionV = MathUtils.linearInterpolation(fraction, startV, endV)
		
		return floatArrayOf(
			fractionH,
			fractionS,
			fractionV
		).fromHsv()
	}
}