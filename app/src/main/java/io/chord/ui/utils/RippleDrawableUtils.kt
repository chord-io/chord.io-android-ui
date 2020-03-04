package io.chord.ui.utils

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import java.util.*


class RippleDrawableUtils
{
	companion object
	{
		fun createRound(
			normalColor: Int,
			pressedColor: Int,
			roundness: Float
		): RippleDrawable
		{
			val radius = FloatArray(8)
			Arrays.fill(radius, roundness)
			val shape = RoundRectShape(radius, null, null)
			val mask = ShapeDrawable(shape)
			
			return RippleDrawableUtils.create(
				normalColor,
				pressedColor,
				mask
			)
		}
		
		fun create(
			normalColor: Int,
			pressedColor: Int,
			mask: Drawable
		): RippleDrawable
		{
			val content = mask.constantState!!.newDrawable().mutate()
			content.setColorFilter(normalColor, PorterDuff.Mode.SRC_IN)
			
			return RippleDrawable(
				getColorStateList(normalColor, pressedColor),
				content,
				mask
			)
		}
		
		fun getColorStateList(
			normalColor: Int,
			pressedColor: Int
		): ColorStateList
		{
			return ColorStateList(
				arrayOf(
					intArrayOf(android.R.attr.state_pressed),
					intArrayOf(android.R.attr.state_focused),
					intArrayOf(android.R.attr.state_activated),
					intArrayOf(android.R.attr.state_selected),
					intArrayOf()
				), intArrayOf(
					pressedColor,
					pressedColor,
					pressedColor,
					pressedColor,
					normalColor
				)
			)
		}
		
		fun getColorDrawableFromColor(color: Int): ColorDrawable
		{
			return ColorDrawable(color)
		}
	}
}