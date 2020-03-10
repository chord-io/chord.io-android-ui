package io.chord.ui.gestures

import android.content.Context
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent

class GestureDetector : GestureDetector
{
	private var listener: io.chord.ui.gestures.SimpleOnGestureListener? = null
	
	constructor(
		context: Context?,
		listener: OnGestureListener?
	) : super(context, listener)
	{
		this.init(listener)
	}
	
	constructor(
		context: Context?,
		listener: OnGestureListener?,
		handler: Handler?
	) : super(context, listener, handler)
	{
		this.init(listener)
	}
	
	constructor(
		context: Context?,
		listener: OnGestureListener?,
		handler: Handler?,
		unused: Boolean
	) : super(context, listener, handler, unused)
	{
		this.init(listener)
	}
	
	private fun init(listener: OnGestureListener?)
	{
		if(listener is io.chord.ui.gestures.SimpleOnGestureListener)
		{
			this.listener = listener
		}
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		if(super.onTouchEvent(event))
		{
			return true
		}
		else if(event.action == MotionEvent.ACTION_UP && this.listener != null)
		{
			return this.listener!!.onUp(event)
		}
		else if(event.action == MotionEvent.ACTION_MOVE && this.listener != null)
		{
			return this.listener!!.onMove(event)
		}
		
		return false
	}
}