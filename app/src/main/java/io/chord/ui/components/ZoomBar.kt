package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import io.chord.R
import io.chord.ui.utils.MathUtils
import io.chord.ui.utils.ViewUtils

class ZoomBar : View
{
	private class ZoomBarGestureListener(
		private val zoomBar: ZoomBar
	) : GestureDetector.SimpleOnGestureListener()
	{
		override fun onDown(event: MotionEvent): Boolean {
			return true
		}
		
		override fun onScroll(
			event1: MotionEvent,
			event2: MotionEvent,
			distanceX: Float,
			distanceY: Float
		): Boolean {
			this.computePosition(event2)
			return true
		}
		
		override fun onDoubleTap(event: MotionEvent): Boolean {
			this.zoomBar.setFactor(this.zoomBar.factors[this.zoomBar.defaultFactorIndex])
			return true
		}
		
		override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
			this.computePosition(event)
			return true
		}
		
		private fun computePosition(event: MotionEvent)
		{
			var position = if(this.zoomBar.orientation == ViewOrientation.Vertical)
			{
				event.y - this.zoomBar.thumbThickness / 2f
			}
			else
			{
				event.x - this.zoomBar.thumbThickness / 2f
			}
			
			val limit = this.zoomBar.getLimit()
			
			position = when
			{
				position < this.zoomBar._getPaddingLeft() -> this.zoomBar._getPaddingLeft().toFloat()
				position > limit -> limit
				else -> position
			}
			
			this.zoomBar.setPosition(position)
		}
	}
	
	private val zoomables: MutableMap<Int, Zoomable> = mutableMapOf()
	
	private val gestureDetector: GestureDetector = GestureDetector(
		this.context,
		ZoomBarGestureListener(this)
	)
	private val painter: Paint = Paint()
	private var position: Float = 0f
	private var factor: Float = -1f
	
	private val defaultFactorIndex = 5
	
	private val factors: List<Float> = listOf(
		0.30f,
		0.50f,
		0.67f,
		0.80f,
		0.90f,
		1.00f,
		1.10f,
		1.20f,
		1.33f,
		1.50f,
		1.70f,
		2.00f,
		2.40f,
		3.00f
	)
	
	private var _orientation: ViewOrientation = ViewOrientation.Horizontal
	private var _trackColor: Int = -1
	private var _color: Int = -1
	private var _trackThickness: Float = -1f
	private var _thumbThickness: Float = -1f
	
	var orientation: ViewOrientation
		get() = this._orientation
		set(value) {
			this._orientation = value
			this.invalidate()
		}
	
	var trackColor: Int
		get() = this._trackColor
		set(value) {
			this._trackColor = value
			this.invalidate()
		}
	
	var color: Int
		get() = this._color
		set(value) {
			this._color = value
			this.invalidate()
		}
	
	var trackThickness: Float
		get() = this._trackThickness
		set(value) {
			this._trackThickness = value
			this.invalidate()
		}
	
	var thumbThickness: Float
		get() = this._thumbThickness
		set(value) {
			this._thumbThickness = value
			this.invalidate()
		}
	
	constructor(context: Context?) : super(context)
	{
		this.init(null, 0)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?
	) : super(context, attrs)
	{
		this.init(attrs, 0)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.init(attrs, defStyleAttr)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.init(attrs, defStyleAttr)
	}
	
	private fun init(attrs: AttributeSet?, defStyle: Int)
	{
		val typedArray = this.context.obtainStyledAttributes(
			attrs, R.styleable.ZoomBar, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this.orientation = typedArray.getInteger(
			R.styleable.ScrollBar_cio_sb_orientation,
			ViewOrientation.Horizontal.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		this.trackColor = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_trackColor,
			this.resources.getColor(R.color.borderColor, theme)
		)
		
		this.color = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_color,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.trackThickness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_trackThickness,
			this.resources.getDimension(R.dimen.zoombar_track_thickness)
		)
		
		this.thumbThickness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_thumbThickness,
			this.resources.getDimension(R.dimen.zoombar_thumb_thickness)
		)
		
		typedArray.recycle()
	}
	
	fun attach(id: Int)
	{
		val rootView = ViewUtils.getParentRootView(this)
		val zoomable = rootView.findViewById<View>(id)
		
		this.zoomables[id] = zoomable as Zoomable
	}
	
	fun detach(id: Int)
	{
		this.zoomables.remove(id)
	}
	
	private fun getSizeWithoutPaddings(): Int
	{
		return if(this.orientation == ViewOrientation.Vertical)
		{
			this.height
		}
		else
		{
			this.width
		}
	}
	
	private fun getSize(): Int
	{
		return this.getSizeWithoutPaddings() - this.getPadding()
	}
	
	private fun _getPaddingLeft(): Int
	{
		return if(this.orientation == ViewOrientation.Vertical)
		{
			this.paddingTop
		}
		else
		{
			this.paddingStart
		}
	}
	
	private fun _getPaddingRight(): Int
	{
		return if(this.orientation == ViewOrientation.Vertical)
		{
			this.paddingBottom
		}
		else
		{
			this.paddingEnd
		}
	}
	
	private fun getLimit(): Float
	{
		return this.getSizeWithoutPaddings() - this._getPaddingRight() - this._thumbThickness
	}
	
	private fun getPadding(): Int
	{
		return this._getPaddingLeft() + this._getPaddingRight()
	}
	
	private fun getSteps(): List<Float>
	{
		val steps = mutableListOf<Float>()
		val stepSize = this.getSize() / this.factors.size.toFloat()
		val limit = this.getLimit()
		
		this.factors.forEachIndexed { index, _ ->
			val step = index * stepSize
			steps.add(index, step)
		}
		
		if(steps.last() != limit)
		{
			steps[steps.size - 1] = limit
		}
		
		return steps
	}
	
	private fun setFactor(factor: Float)
	{
		val steps = this.getSteps()
		val index = this.factors.indexOf(factor)
		this.factor = factor
		this.position = steps[index]
		this.invalidate()
	}
	
	private fun setPosition(position: Float)
	{
		val steps = this.getSteps()
		val step = MathUtils.step(position, steps)
		val index = steps.indexOf(step)
		this.factor = this.factors[index]
		this.position = step
		this.invalidate()
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		return if (this.gestureDetector.onTouchEvent(event))
		{
			true
		}
		else
		{
			super.onTouchEvent(event)
		}
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		if(this.factor == -1f)
		{
			this.setFactor(this.factors[this.defaultFactorIndex])
		}
		
		this.drawTrack(canvas)
		
		this.drawThumb(canvas)
		
		this.invalidate()
	}
	
	private fun drawTrack(canvas: Canvas?)
	{
		this.trackColor.let {
			this.painter.color = it
		}
		
		this.trackThickness.let {
			this.painter.strokeWidth = it
		}
		
		this.painter.strokeCap = Paint.Cap.ROUND
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			val centerVertical = this.height / 2f
			val left = this.paddingStart.toFloat()
			val right = this.width - this.paddingEnd.toFloat()
			canvas?.drawLine(
				left,
				centerVertical,
				right,
				centerVertical,
				this.painter
			)
		}
		else
		{
			val centerHorizontal = this.width / 2f
			val top = this.paddingTop.toFloat()
			val bottom = this.height - this.paddingBottom.toFloat()
			canvas?.drawLine(
				centerHorizontal,
				top,
				centerHorizontal,
				bottom,
				this.painter
			)
		}
	}
	
	private fun drawThumb(canvas: Canvas?)
	{
		this.color.let {
			this.painter.color = it
		}

		val thickness = this.thumbThickness
		val position = this.position
		val roundness = ViewUtils.dipToPixel(
			this,
			this.resources.getDimension(R.dimen.zoombar_thumb_roundness)
		)

		if(this.orientation == ViewOrientation.Horizontal)
		{
			val centerVertical = (this.height / 2f) - (thickness / 2f)
			val left = position + this.paddingStart
			val right = position + thickness - this.paddingEnd
			canvas?.drawRoundRect(
				left,
				centerVertical,
				right,
				centerVertical + thickness,
				roundness,
				roundness,
				this.painter
			)
		}
		else
		{
			val centerHorizontal = (this.width / 2f) - (thickness / 2f)
			val top = position + this.paddingTop
			val bottom = position + thickness - this.paddingBottom
			canvas?.drawRoundRect(
				centerHorizontal,
				top,
				centerHorizontal + thickness,
				bottom,
				roundness,
				roundness,
				this.painter
			)
		}
	}
}