package io.chord.ui.gestures

import android.view.GestureDetector
import android.view.MotionEvent

open class SimpleOnGestureListener : GestureDetector.SimpleOnGestureListener()
{
	open fun onUp(event: MotionEvent): Boolean
	{
		return false
	}
	
	open fun onMove(event: MotionEvent): Boolean
	{
		return false
	}
}