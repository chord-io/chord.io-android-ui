package io.chord.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.chord.R
import io.chord.ui.gestures.GestureDetector
import io.chord.ui.gestures.SimpleOnGestureListener
import io.chord.ui.utils.ColorUtils
import io.chord.ui.utils.MathUtils
import io.chord.ui.utils.ViewUtils


class ZoomBar : View, Binder
{
	private class FocusListener(
		private val zoomBar: ZoomBar
	) : OnFocusChangeListener
	{
		init
		{
			this.zoomBar.focusEnterAnimator.setFloatValues(0f, 1f)
			this.zoomBar.focusEnterAnimator.addUpdateListener { animator ->
				val opacity = animator.animatedValue as Float
				this.setOpacity(opacity)
			}
			
			this.zoomBar.focusExitAnimator.setFloatValues(1f, 0f)
			this.zoomBar.focusExitAnimator.addUpdateListener { animator ->
				val opacity = animator.animatedValue as Float
				this.setOpacity(opacity)
			}
			
			this.setOpacity(0f)
		}
		
		override fun onFocusChange(
			view: View?,
			hasFocus: Boolean
		)
		{
			if(hasFocus)
			{
				this.zoomBar.focusEnterAnimator.start()
			}
			else
			{
				this.zoomBar.focusExitAnimator.start()
			}
		}
		
		private fun setOpacity(opacity: Float)
		{
			this.zoomBar._bubbleBackgroundColor = ColorUtils.toTransparent(
				this.zoomBar._bubbleBackgroundColor,
				opacity
			)
			
			this.zoomBar._bubbleTextColor = ColorUtils.toTransparent(
				this.zoomBar._bubbleTextColor,
				opacity
			)
			
			this.zoomBar.invalidate()
		}
	}
	
	private class GestureListener(
		private val zoomBar: ZoomBar
	) : SimpleOnGestureListener()
	{
		private var positionSource: Float = 0f
		private var positionDestination: Float = 0f
		
		init
		{
			this.zoomBar.positionAnimator.addUpdateListener {
				val position = it.animatedValue as Float
				this.zoomBar.position = position
				this.zoomBar.invalidate()
			}
			
			// TODO : set interpolator on another place
			this.zoomBar.positionAnimator.interpolator = FastOutSlowInInterpolator()
		}
		
		override fun onDown(event: MotionEvent): Boolean {
			this.zoomBar.requestFocus()
			return true
		}
		
		override fun onUp(event: MotionEvent): Boolean
		{
			this.zoomBar.clearFocus()
			
			if(this.zoomBar.isScrolling)
			{
				this.zoomBar.isScrolling = false
			}
			
			return true
		}
		
		override fun onScroll(
			event1: MotionEvent,
			event2: MotionEvent,
			distanceX: Float,
			distanceY: Float
		): Boolean {
			if(!this.zoomBar.isScrolling)
			{
				this.zoomBar.isScrolling = true
			}
			
			this.setPosition(event2)
			return true
		}
		
		override fun onDoubleTap(event: MotionEvent): Boolean {
			this.beginConfigureAnimation()
			this.zoomBar.setFactor(this.zoomBar.factors[this.zoomBar.defaultFactorIndex])
			this.endConfigureAnimation()
			return true
		}
		
		override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
			this.beginConfigureAnimation()
			this.setPosition(event)
			this.endConfigureAnimation()
			return true
		}
		
		private fun beginConfigureAnimation()
		{
			this.positionSource = this.zoomBar.position
		}
		
		private fun endConfigureAnimation()
		{
			this.positionDestination = this.zoomBar.position
			this.zoomBar.positionAnimator.setFloatValues(
				this.positionSource,
				this.positionDestination
			)
			this.zoomBar.positionAnimator.start()
		}
		
		private fun setPosition(event: MotionEvent)
		{
			val limit = this.zoomBar.getLimit()
			var position = if(this.zoomBar.orientation == ViewOrientation.Vertical)
			{
				event.y - this.zoomBar.thumbThickness / 2f
			}
			else
			{
				event.x - this.zoomBar.thumbThickness / 2f
			}
			
			position = when
			{
				position < this.zoomBar._getPaddingLeft() -> this.zoomBar._getPaddingLeft().toFloat()
				position > limit -> limit
				else -> position
			}
			
			this.zoomBar.setPositionWithoutInvalidate(position)
		}
	}
	
	private val zoomables: MutableMap<Int, Zoomable> = mutableMapOf()
	
	private val positionAnimator: ValueAnimator = ValueAnimator()
	private val focusEnterAnimator: ValueAnimator = ValueAnimator()
	private val focusExitAnimator: ValueAnimator = ValueAnimator()
	private val gestureDetector: GestureDetector = GestureDetector(
		this.context,
		GestureListener(this)
	)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private var position: Float = 0f
	private var factor: Float = -1f
	private var isScrolling: Boolean = false
	
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
	private var _moveDuration: Long = -1
	private var _focusEnterDuration: Long = -1
	private var _focusExitDuration: Long = -1
	private var _trackColor: Int = -1
	private var _ticksColor: Int = -1
	private var _bubbleBackgroundColor: Int = -1
	private var _bubbleTextColor: Int = -1
	private var _thumbColor: Int = -1
	private var _trackThickness: Float = -1f
	private var _ticksThickness: Float = -1f
	private var _ticksPadding: Float = -1f
	private var _thumbThickness: Float = -1f
	private var _bubbleThickness: Float = -1f
	private var _bubbleRoundness: Float = -1f
	private var _bubblePadding: Float = -1f
	private var _bubbleMargin: Float = -1f
	private var _bubbleTextSize: Float = -1f
	private var _bubbleInvert: Boolean = false
	
	var orientation: ViewOrientation
		get() = this._orientation
		set(value) {
			this._orientation = value
			this.invalidate()
		}
	
	var moveDuration: Long
		get() = this._moveDuration
		set(value) {
			this._moveDuration = value
			this.positionAnimator.duration = value
		}
	
	var focusEnterDuration: Long
		get() = this._focusEnterDuration
		set(value) {
			this._focusEnterDuration = value
			this.focusEnterAnimator.startDelay = value
			this.focusEnterAnimator.duration = value
		}
	
	var focusExitDuration: Long
		get() = this._focusExitDuration
		set(value) {
			this._focusExitDuration = value
			this.focusExitAnimator.startDelay = value
			this.focusExitAnimator.duration = value
		}
	
	var trackColor: Int
		get() = this._trackColor
		set(value) {
			this._trackColor = value
			this.invalidate()
		}
	
	var ticksColor: Int
		get() = this._ticksColor
		set(value) {
			this._ticksColor = value
			this.invalidate()
		}
	
	var thumbColor: Int
		get() = this._thumbColor
		set(value) {
			this._thumbColor = value
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
	
	var trackThickness: Float
		get() = this._trackThickness
		set(value) {
			this._trackThickness = value
			this.invalidate()
		}
	
	var ticksThickness: Float
		get() = this._ticksThickness
		set(value) {
			this._ticksThickness = value
			this.invalidate()
		}
	
	var ticksPadding: Float
		get() = this._ticksPadding
		set(value) {
			this._ticksPadding = value
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
	
	var bubbleInvert: Boolean
		get() = this._bubbleInvert
		set(value) {
			this._bubbleInvert = value
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
		
		// TODO change affection public to private properties
		
		this.orientation = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_orientation,
			ViewOrientation.Horizontal.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		this.moveDuration = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_moveDuration,
			this.resources.getInteger(R.integer.zoombar_move_duration)
		).toLong()
		
		this.focusEnterDuration = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_focusEnterDuration,
			this.resources.getInteger(R.integer.zoombar_focus_enter_duration)
		).toLong()
		
		this.focusExitDuration = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_focusExitDuration,
			this.resources.getInteger(R.integer.zoombar_focus_exit_duration)
		).toLong()
		
		this.trackColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_trackColor,
			this.resources.getColor(R.color.borderColor, theme)
		)
		
		this.ticksColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_ticksColor,
			this.resources.getColor(R.color.borderColor, theme)
		)
		
		this.thumbColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_thumbColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.bubbleBackgroundColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_bubbleBackgroundColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.bubbleTextColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_bubbleTextColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this.trackThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_trackThickness,
			this.resources.getDimension(R.dimen.zoombar_track_thickness)
		)
		
		this.ticksThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_ticksThickness,
			this.resources.getDimension(R.dimen.zoombar_ticks_thickness)
		)
		
		this.ticksPadding = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_ticksPadding,
			this.resources.getDimension(R.dimen.zoombar_ticks_padding)
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
		
		this.bubbleInvert = typedArray.getBoolean(
			R.styleable.ZoomBar_cio_zb_bubbleInvert,
			false
		)
		
		typedArray.recycle()
		
		this.isFocusable = true
		this.isFocusableInTouchMode = true
		this.onFocusChangeListener = FocusListener(this)
	}
	
	override fun attach(id: Int)
	{
		val rootView = ViewUtils.getParentRootView(this)
		val zoomable = rootView.findViewById<View>(id)
		this.zoomables[id] = zoomable as Zoomable
	}
	
	override fun detach(id: Int)
	{
		this.zoomables.remove(id)
	}
	
	private fun dispatchEvent()
	{
		this.zoomables.forEach { (_, zoomable) ->
			zoomable.setZoomFactor(this.orientation, this.factor, !this.isScrolling)
		}
	}
	
	private fun getSizeWithoutPadding(): Int
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
		return this.getSizeWithoutPadding() - this.getPadding()
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
		return this.getSizeWithoutPadding() - this._getPaddingRight() - this._thumbThickness
	}
	
	private fun getPadding(): Int
	{
		return this._getPaddingLeft() + this._getPaddingRight()
	}
	
	private fun getSteps(): List<Float>
	{
		val steps = mutableListOf<Float>()
		val limit = this.getLimit()
		val size = this.getSizeWithoutPadding().toFloat()
		val paddingLeft = this._getPaddingLeft().toFloat()
		val stepSize = size / (this.factors.size.toFloat() - 1)
		
		steps.add(0f)
		
		for(index in 1 until this.factors.size)
		{
			val step = index * stepSize
			steps.add(index, step)
		}
		
		return steps.map {
			MathUtils.map(
				it,
				0f,
				size,
				paddingLeft,
				limit
			)
		}
	}
	
	private fun setFactor(factor: Float)
	{
		val steps = this.getSteps()
		val index = this.factors.indexOf(factor)
		this.factor = factor
		this.position = steps[index]
		this.invalidate()
		
		this.dispatchEvent()
	}
	
	private fun setPositionWithoutInvalidate(position: Float)
	{
		val steps = this.getSteps()
		val step = MathUtils.nearest(position, steps)
		val index = steps.indexOf(step)
		this.factor = this.factors[index]
		this.position = step
		
		this.dispatchEvent()
	}
	
	private fun setPosition(position: Float)
	{
		this.setPositionWithoutInvalidate(position)
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
		
		this.drawTicks(canvas!!)
		this.drawTrack(canvas)
		this.drawThumb(canvas)
		this.drawBubble(canvas)
		
		this.invalidate()
	}
	
	private fun drawTrack(canvas: Canvas)
	{
		this.painter.color = this.trackColor
		this.painter.strokeWidth = this.trackThickness
		this.painter.strokeCap = Paint.Cap.ROUND
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			val centerVertical = this.height / 2f
			val left = this.paddingStart.toFloat()
			val right = this.width - this.paddingEnd.toFloat()
			canvas.drawLine(
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
			canvas.drawLine(
				centerHorizontal,
				top,
				centerHorizontal,
				bottom,
				this.painter
			)
		}
	}
	
	private fun drawTicks(canvas: Canvas)
	{
		if(this.ticksThickness == 0f)
		{
			return
		}
		
		this.painter.color = this.ticksColor
		this.painter.strokeWidth = this.ticksThickness
		this.painter.strokeCap = Paint.Cap.ROUND
		
		val steps = this.getSteps()
		val bounds = canvas.clipBounds.toRectF()
		val halfThumbThickness = this.thumbThickness / 2f
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			val top = bounds.top + this.ticksPadding
			val bottom = bounds.bottom - this.ticksPadding
			
			steps.forEach { position ->
				val x = position + halfThumbThickness
				canvas.drawLine(
					x,
					bottom,
					x,
					top,
					this.painter
				)
			}
		}
		else
		{
			val left = bounds.left + this.ticksPadding
			val right = bounds.right - this.ticksPadding
			
			steps.forEach { position ->
				val y = position + halfThumbThickness
				canvas.drawLine(
					right,
					y,
					left,
					y,
					this.painter
				)
			}
		}
	}
	
	private fun drawThumb(canvas: Canvas)
	{
		this.painter.color = this.thumbColor

		val thickness = this.thumbThickness
		val position = this.position
		val roundness = ViewUtils.dpToPixel(this.resources.getDimension(R.dimen.zoombar_thumb_roundness))

		if(this.orientation == ViewOrientation.Horizontal)
		{
			val centerVertical = (this.height / 2f) - (thickness / 2f)
			val left = position
			val right = left + thickness
			canvas.drawRoundRect(
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
			val top = position
			val bottom = top + thickness
			canvas.drawRoundRect(
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
	
	private fun drawBubble(canvas: Canvas)
	{
		this.painter.color = this.bubbleBackgroundColor
		this.painter.textSize = this.bubbleTextSize
		this.painter.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
		
		val bounds = Rect(canvas.clipBounds).toRectF()
		val position = this.position + this.thumbThickness / 2
		val label = this.factor.toString()
		val width = (ViewUtils.getTextWidth(label, this.painter) + this.bubblePadding).toInt()
		val halfWidth = width / 2f
		val height = this.bubbleThickness.toInt()
		val halfHeight = height / 2f
		val textHeight = ViewUtils.getTextHeight(label, this.painter)

		if(this._orientation == ViewOrientation.Horizontal)
		{
			val offset = bounds.height() + this.bubbleMargin
			val left = bounds.left + position - halfWidth
			val right = left + width
			val translatedBounds: RectF?
			
			if(this.bubbleInvert)
			{
				translatedBounds = RectF(
					bounds.left - halfWidth,
					bounds.top + offset,
					bounds.right + halfWidth,
					bounds.top + offset + height
				)
			}
			else
			{
				translatedBounds = RectF(
					bounds.left - halfWidth,
					bounds.bottom - offset - height,
					bounds.right + halfWidth,
					bounds.bottom - offset
				)
			}
			
			val rectBackgroundBubble = RectF(
				left,
				translatedBounds.top,
				right,
				translatedBounds.bottom
			)
			
			canvas.save()
			
			canvas.clipRect(translatedBounds)
			
			canvas.drawRoundRect(
				rectBackgroundBubble.left,
				rectBackgroundBubble.top,
				rectBackgroundBubble.right,
				rectBackgroundBubble.bottom,
				this.bubbleRoundness,
				this.bubbleRoundness,
				this.painter
			)
			
			this.painter.color = this.bubbleTextColor

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
			val offset = bounds.width() + this.bubbleMargin
			val top = bounds.top + position - halfHeight
			val bottom = top + height
			val translatedBounds: RectF?
			
			if(this.bubbleInvert)
			{
				translatedBounds = RectF(
					bounds.left + offset,
					bounds.top - halfHeight,
					bounds.left + offset + width,
					bounds.bottom + halfHeight
				)
			}
			else
			{
				translatedBounds = RectF(
					bounds.right - offset - width,
					bounds.top - halfHeight,
					bounds.right - offset,
					bounds.bottom + halfHeight
				)
			}
			
			val rectBackgroundBubble = RectF(
				translatedBounds.left,
				top,
				translatedBounds.right,
				bottom
			)
			
			canvas.save()
			
			canvas.clipRect(translatedBounds)
			
			canvas.drawRoundRect(
				rectBackgroundBubble.left,
				rectBackgroundBubble.top,
				rectBackgroundBubble.right,
				rectBackgroundBubble.bottom,
				this.bubbleRoundness,
				this.bubbleRoundness,
				this.painter
			)
			
			this.painter.color = this.bubbleTextColor
			
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