package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
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
	
	private var isInitialized: Boolean = false
	
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
		this.setContentView(children)
	}
	
	override fun addView(child: View)
	{
		super.addView(child)
		this.setContentView(child)
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		val x = this.horizontalScrollView.onSuperToucEvent(event)
		val y = this.verticalScrollView.onSuperToucEvent(event)
		return x || y
	}
	
	private fun setContentView(children: View)
	{
		(children.parent as ViewGroup).removeView(children)
		
		if(!this.isInitialized)
		{
			val view = LayoutInflater.from(this.context).inflate(
				R.layout.component_two_dimensional_scroll_view,
				this,
				true
			)
			this.verticalScrollView = view.findViewById(R.id.vertical_scroll_view)
			this.horizontalScrollView = view.findViewById(R.id.horizontal_scroll_view)
			this.horizontalScrollView.addView(children)
			this.isInitialized = true
		}
		else
		{
			this.horizontalScrollView.removeAllViews()
			this.horizontalScrollView.addView(children)
		}
	}
	
	fun hasContent(): Boolean
	{
		return try
		{
			this.horizontalScrollView.childCount == 1
		}
		catch(exception: UninitializedPropertyAccessException)
		{
			false
		}
	}
}