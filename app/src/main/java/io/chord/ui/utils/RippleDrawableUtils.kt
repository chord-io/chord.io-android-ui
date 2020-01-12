package io.chord.ui.utils

import android.R
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable


class RippleDrawableUtils
{
	companion object
	{
		fun create(
			normalColor: Int,
			pressedColor: Int
		): RippleDrawable?
		{
			return RippleDrawable(
				getColorStateList(normalColor, pressedColor)!!,
				getColorDrawableFromColor(normalColor),
				null
			)
		}
		
		fun getColorStateList(
			normalColor: Int,
			pressedColor: Int
		): ColorStateList?
		{
			return ColorStateList(
				arrayOf(
					intArrayOf(R.attr.state_pressed),
					intArrayOf(R.attr.state_focused),
					intArrayOf(R.attr.state_activated),
					intArrayOf()
				), intArrayOf(
					pressedColor,
					pressedColor,
					pressedColor,
					normalColor
				)
			)
		}
		
		private fun getColorDrawableFromColor(color: Int): ColorDrawable?
		{
			return ColorDrawable(color)
		}
	}
}