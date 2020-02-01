package io.chord.ui.extensions

import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import java.util.*

fun RippleDrawable.setBackgroundColor(color: Int)
{
	val content = this.getDrawable(0)
	content.setColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun RippleDrawable.setRoundness(value: Float)
{
	val radius = FloatArray(8)
	Arrays.fill(radius, value)
	val shape = RoundRectShape(radius, null, null)
	val mask = ShapeDrawable(shape)
	
	val contentColor = this.getDrawable(0).colorFilter
	val content = mask.constantState!!.newDrawable().mutate()
	content.colorFilter = contentColor
	
	this.setDrawable(0, content)
	this.setDrawable(1, mask)
}