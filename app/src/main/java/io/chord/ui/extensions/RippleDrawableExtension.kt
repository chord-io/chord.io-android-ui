package io.chord.ui.extensions

import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable

fun RippleDrawable.setBackgroundColor(color: Int)
{
	val content = this.getDrawable(0)
	content.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}