package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import io.chord.ui.behaviors.StateBehavior
import io.chord.ui.behaviors.SurfaceGestureBehavior

class StateScrollView : TwoDimensionalScrollView, Modulable<EditorMode>
{
	private val gestureBehavior = SurfaceGestureBehavior(this.context)
	private val stateBehavior = StateBehavior(this.gestureBehavior)
	
	constructor(context: Context) : super(context)
	
	constructor(
		context: Context,
		attrs: AttributeSet?
	) : super(context, attrs)
	
	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	
	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	
	override fun setMode(mode: EditorMode)
	{
		this.stateBehavior.setMode(mode)
	}
	
	override fun onInterceptTouchEvent(event: MotionEvent): Boolean
	{
		return this.stateBehavior.mode == EditorMode.Move
	}
}