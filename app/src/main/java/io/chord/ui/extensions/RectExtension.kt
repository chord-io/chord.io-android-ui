package io.chord.ui.extensions

import android.graphics.Rect
import android.graphics.RectF

fun Rect.clamp(other: Rect)
{
	if(this.left < other.left) this.left = other.left
	if(this.top < other.top) this.top = other.top
	if(this.right > other.right) this.right = other.right
	if(this.bottom > other.bottom) this.bottom = other.bottom
}

fun RectF.translate(x: Float, y: Float)
{
	this.set(
		this.left + x,
		this.top + y,
		this.right + x,
		this.bottom + y
	)
}

fun Rect.translate(x: Int, y: Int)
{
	this.set(
		this.left + x,
		this.top + y,
		this.right + x,
		this.bottom + y
	)
}