package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.HorizontalScrollView


class HorizontalScrollView : HorizontalScrollView
{
	constructor(context: Context?) : super(context)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?
	) : super(context, attrs)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	
	fun onSuperToucEvent(event: MotionEvent): Boolean
	{
		return super.onTouchEvent(event)
	}
	
	override fun onTouchEvent(event: MotionEvent?): Boolean
	{
		return false
	}
	
	override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
	{
		return false
	}
}