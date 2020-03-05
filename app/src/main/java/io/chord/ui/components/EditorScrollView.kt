package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class EditorScrollView : TwoDimensionalScrollView, Modulable<EditorMode>
{
	private val gesture = EditorGesture(this.context)
	private val stateContext = EditorState(this.gesture)
	
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
		this.stateContext.setMode(mode)
	}
	
	override fun onInterceptTouchEvent(event: MotionEvent): Boolean
	{
		return this.stateContext.mode == EditorMode.Move
	}
}