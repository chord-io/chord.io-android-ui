package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.chord.R
import io.chord.ui.extensions.getDirectChildren


class TwoDimensionalScrollView : LinearLayout
{
	private var _x: Float = 0f
	private var _y: Float = 0f
	lateinit var verticalScrollView: VerticalScrollView
	lateinit var horizontalScrollView: HorizontalScrollView
	
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
		val currentX: Float
		val currentY: Float
		
		when(event.action)
		{
			MotionEvent.ACTION_DOWN ->
			{
				this._x = event.x
				this._y = event.y
			}
			MotionEvent.ACTION_MOVE ->
			{
				currentX = event.x
				currentY = event.y
				this.verticalScrollView.scrollBy(
					(this._x - currentX).toInt(),
					(this._y - currentY).toInt()
				)
				this.horizontalScrollView.scrollBy(
					(this._x - currentX).toInt(),
					(this._y - currentY).toInt()
				)
				this._x = currentX
				this._y = currentY
			}
			MotionEvent.ACTION_UP ->
			{
				currentX = event.x
				currentY = event.y
				this.verticalScrollView.scrollBy(
					(this._x - currentX).toInt(),
					(this._y - currentY).toInt()
				)
				this.horizontalScrollView.scrollBy(
					(this._x - currentX).toInt(),
					(this._y - currentY).toInt()
				)
			}
		}
		return true
	}
}