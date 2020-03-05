package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.chord.R
import io.chord.ui.extensions.getDirectChildren


open class TwoDimensionalScrollView : LinearLayout
{
	lateinit var verticalScrollView: VerticalScrollView
	lateinit var horizontalScrollView: HorizontalScrollView
	
	constructor(context: Context) : super(context)
	{
		this.init()
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet?
	) : super(context, attrs)
	{
		this.init()
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.init()
	}
	
	constructor(
		context: Context,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.init()
	}
	
	private fun init()
	{
		this.isClickable = true
		this.isFocusable = true
	}
	
	override fun onFinishInflate()
	{
		super.onFinishInflate()
		
		val children = this.getDirectChildren() ?: return
		(children.parent as ViewGroup).removeView(children)
		val view = View.inflate(context, R.layout.component_two_dimensional_scroll_view, this)
		this.verticalScrollView = view.findViewById(R.id.vertical_scroll_view)
		this.horizontalScrollView = view.findViewById(R.id.horizontal_scroll_view)
		this.horizontalScrollView.addView(children)
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		val x = this.horizontalScrollView.onSuperToucEvent(event)
		val y = this.verticalScrollView.onSuperToucEvent(event)
		return x || y
	}
}