package io.chord.ui.components

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
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
	private var _bubbleBackgroundColor: Int = -1
	private var _bubbleTextColor: Int = -1
	private var _thumbColor: Int = -1
	private var _trackThickness: Float = -1f
	private var _thumbThickness: Float = -1f
	private var _bubbleThickness: Float = -1f
	private var _bubbleRoundness: Float = -1f
	private var _bubblePadding: Float = -1f
	private var _bubbleMargin: Float = -1f
	private var _bubbleTextSize: Float = -1f
	
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
	
	var bubbleBackgroundColor: Int
		get() = this._bubbleBackgroundColor
		set(value) {
			this._bubbleBackgroundColor = value
			this.invalidate()
		}
	
	var bubbleTextColor: Int
		get() = this._bubbleTextColor
		set(value) {
			this._bubbleTextColor = value
			this.invalidate()
		}
	
	var thumbColor: Int
		get() = this._thumbColor
		set(value) {
			this._thumbColor = value
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
	
	var bubbleThickness: Float
		get() = this._bubbleThickness
		set(value) {
			this._bubbleThickness = value
			this.invalidate()
		}
	
	var bubbleRoundness: Float
		get() = this._bubbleRoundness
		set(value) {
			this._bubbleRoundness = value
			this.invalidate()
		}
	
	var bubblePadding: Float
		get() = this._bubblePadding
		set(value) {
			this._bubblePadding = value
			this.invalidate()
		}
	
	var bubbleMargin: Float
		get() = this._bubbleMargin
		set(value) {
			this._bubbleMargin = value
			this.invalidate()
		}
	
	var bubbleTextSize: Float
		get() = this._bubbleTextSize
		set(value) {
			this._bubbleTextSize = value
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
			R.styleable.ZoomBar_cio_zb_orientation,
			ViewOrientation.Horizontal.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		this.trackColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_trackColor,
			this.resources.getColor(R.color.borderColor, theme)
		)
		
		this.bubbleBackgroundColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_bubbleBackgroundColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.bubbleTextColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_bubbleTextColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this.thumbColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_thumbColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.trackThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_trackThickness,
			this.resources.getDimension(R.dimen.zoombar_track_thickness)
		)
		
		this.thumbThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_thumbThickness,
			this.resources.getDimension(R.dimen.zoombar_thumb_thickness)
		)
		
		this.bubbleThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleThickness,
			this.resources.getDimension(R.dimen.zoombar_bubble_thickness)
		)
		
		this.bubbleRoundness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleRoundness,
			this.resources.getDimension(R.dimen.zoombar_bubble_roundness)
		)
		
		this.bubblePadding = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubblePadding,
			this.resources.getDimension(R.dimen.zoombar_bubble_padding)
		)
		
		this.bubbleMargin = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleMargin,
			this.resources.getDimension(R.dimen.zoombar_bubble_margin)
		)
		
		this.bubbleTextSize = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleTextSize,
			this.resources.getDimension(R.dimen.zoombar_bubble_text_size)
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
		this.drawBubble(canvas)
		
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
		this.thumbColor.let {
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
	
	private fun drawBubble(canvas: Canvas?)
	{
		// TODO: invert bubble to a specific direction
		
		this.bubbleTextSize.let {
			this.painter.textSize = it
			this.painter.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
		}
		
		val rect = Rect(canvas!!.clipBounds)
		val position = this.position + this.thumbThickness / 2
		val label = this.factor.toString()
		val width = (ViewUtils.getTextWidth(label, this.painter) + this.bubblePadding).toInt()
		val halfWidth = width / 2
		val height = this.bubbleThickness.toInt()
		val halfHeight = height / 2
		val textHeight = ViewUtils.getTextHeight(label, this.painter)
		
		this.bubbleBackgroundColor.let {
			this.painter.color = it
		}

		if(this._orientation == ViewOrientation.Horizontal)
		{
			val offset = rect.height() + this.bubbleMargin.toInt()
			val left = rect.left + position - halfWidth
			val right = left + width
			
			rect.set(
				rect.left - halfWidth,
				rect.bottom - offset - height,
				rect.right + halfWidth,
				rect.bottom - offset
			)
			
			val rectBackgroundBubble = RectF(
				left,
				rect.top.toFloat(),
				right,
				rect.bottom.toFloat()
			)
			
			canvas.save()
			
			canvas.clipRect(rect)
			
			canvas.drawRoundRect(
				rectBackgroundBubble.left,
				rectBackgroundBubble.top,
				rectBackgroundBubble.right,
				rectBackgroundBubble.bottom,
				this.bubbleRoundness,
				this.bubbleRoundness,
				this.painter
			)

			this.bubbleTextColor.let {
				this.painter.color = it
			}

			val textPosition = ViewUtils.getTextCentered(
				label,
				rectBackgroundBubble.centerX().toInt(),
				0,
				this.painter
			)

			canvas.drawText(
				label,
				textPosition.x,
				rectBackgroundBubble.centerY() + textHeight / 2f,
				this.painter
			)
			
			canvas.restore()
		}
		else
		{
			val offset = rect.width() + this.bubbleMargin.toInt()
			val top = rect.top + position - halfHeight
			val bottom = top + height
			
			rect.set(
				rect.right - offset - width,
				rect.top - halfHeight,
				rect.right - offset,
				rect.bottom + halfHeight
			)
			
			val rectBackgroundBubble = RectF(
				rect.left.toFloat(),
				top,
				rect.right.toFloat(),
				bottom
			)
			
			canvas.save()
			
			canvas.clipRect(rect)
			
			canvas.drawRoundRect(
				rectBackgroundBubble.left,
				rectBackgroundBubble.top,
				rectBackgroundBubble.right,
				rectBackgroundBubble.bottom,
				this.bubbleRoundness,
				this.bubbleRoundness,
				this.painter
			)

			this.bubbleTextColor.let {
				this.painter.color = it
			}


			val textPosition = ViewUtils.getTextCentered(
				label,
				rectBackgroundBubble.centerX().toInt(),
				rectBackgroundBubble.centerY().toInt(),
				this.painter
			)

			canvas.drawText(
				label,
				textPosition.x,
				textPosition.y,
				this.painter
			)
			
			canvas.restore()
		}
	}
}