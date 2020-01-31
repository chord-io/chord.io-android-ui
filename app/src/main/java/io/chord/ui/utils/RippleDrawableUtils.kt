package io.chord.ui.utils

import android.R
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable


class RippleDrawableUtils
{
	companion object
	{
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
		
		fun setBackgroundColor(ripple: RippleDrawable, color: Int)
		{
			val content = ripple.getDrawable(0)
			content.setColorFilter(color, PorterDuff.Mode.SRC_IN)
		}
		
		fun getColorStateList(
			normalColor: Int,
			pressedColor: Int
		): ColorStateList
		{
			return ColorStateList(
				arrayOf(
					intArrayOf(R.attr.state_pressed),
					intArrayOf(R.attr.state_focused),
					intArrayOf(R.attr.state_activated),
					intArrayOf(R.attr.state_selected),
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