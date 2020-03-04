package io.chord.ui.extensions

import android.graphics.Rect

fun Rect.clamp(other: Rect): Rect
{
	val rect = Rect(this)
	
	if(rect.left < other.left) rect.left = other.left
	if(rect.top < other.top) rect.top = other.top
	if(rect.right > other.right) rect.right = other.right
	if(rect.bottom > other.bottom) rect.bottom = other.bottom
	
	return rect
}