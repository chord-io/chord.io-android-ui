package io.chord.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.ui.animations.FastOutSlowInValueAnimator
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Binder
import io.chord.ui.behaviors.OrientedBoundBehavior
import io.chord.ui.behaviors.PropertyBehavior
import io.chord.ui.behaviors.StepPositionBehavior
import io.chord.ui.extensions.alignCenter
import io.chord.ui.extensions.dpToPixel
import io.chord.ui.extensions.getTextBounds
import io.chord.ui.extensions.toTransparent
import io.chord.ui.gestures.GestureDetector
import io.chord.ui.gestures.SimpleOnGestureListener
import io.chord.ui.utils.MathUtils


class ZoomBar : View, Binder
{
	private class ZoomBarBoundBehavior(
		private val zoomBar: ZoomBar
	) : OrientedBoundBehavior(zoomBar)
	{
		fun getLimit(): Float
		{
			return this.getSizeWithoutPadding() -
					this.getPaddingEnd() -
					this.zoomBar._thumbThickness
		}
		
		fun getSteps(): List<Float>
		{
			val steps = mutableListOf<Float>()
			val limit = this.getLimit()
			val size = this.getSizeWithoutPadding().toFloat()
			val paddingLeft = this.getPaddingStart().toFloat()
			val stepSize = size / (this.zoomBar.factors.size.toFloat() - 1)
			
			steps.add(0f)
			
			for(index in 1 until this.zoomBar.factors.size)
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
	}
	
	private class FocusListener(
		private val zoomBar: ZoomBar
	) : OnFocusChangeListener
	{
		val focusEnterAnimator: ValueAnimator = FastOutSlowInValueAnimator()
		val focusExitAnimator: ValueAnimator = FastOutSlowInValueAnimator()
		
		init
		{
			this.focusEnterAnimator.setFloatValues(0f, 1f)
			this.focusEnterAnimator.addUpdateListener { animator ->
				val opacity = animator.animatedValue as Float
				this.setOpacity(opacity)
			}
			
			this.focusExitAnimator.setFloatValues(1f, 0f)
			this.focusExitAnimator.addUpdateListener { animator ->
				val opacity = animator.animatedValue as Float
				this.setOpacity(opacity)
			}
		}
		
		override fun onFocusChange(
			view: View?,
			hasFocus: Boolean
		)
		{
			if(hasFocus)
			{
				this.focusEnterAnimator.start()
			}
			else
			{
				this.focusExitAnimator.start()
			}
		}
		
		fun setOpacity(opacity: Float)
		{
			this.zoomBar._bubbleBackgroundColor = this.zoomBar
				._bubbleBackgroundColor
				.toTransparent(
					opacity
				)
			
			this.zoomBar._bubbleTextColor = this.zoomBar
				._bubbleTextColor.toTransparent(
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
		
		var isScrolling: Boolean = false
		
		val positionAnimator: ValueAnimator = FastOutSlowInValueAnimator()
		
		init
		{
			this.positionAnimator.addUpdateListener {
				val position = it.animatedValue as Float
				this.zoomBar.positionBehavior.setPosition(position)
				this.zoomBar.invalidate()
			}
		}
		
		override fun onDown(event: MotionEvent): Boolean {
			this.zoomBar.requestFocus()
			this.isScrolling = false
			return true
		}
		
		override fun onUp(event: MotionEvent): Boolean
		{
			this.zoomBar.clearFocus()
			this.isScrolling = false
			return true
		}
		
		override fun onScroll(
			event1: MotionEvent,
			event2: MotionEvent,
			distanceX: Float,
			distanceY: Float
		): Boolean {
			this.isScrolling = true
			this.setPosition(event2)
			this.zoomBar.bindBehavior.requestDispatchEvent()
			this.zoomBar.invalidate()
			return true
		}
		
		override fun onDoubleTap(event: MotionEvent): Boolean {
			this.beginConfigureAnimation()
			this.zoomBar.positionBehavior.setIndex(this.zoomBar.defaultFactorIndex)
			this.zoomBar.bindBehavior.requestDispatchEvent()
			this.endConfigureAnimation()
			return true
		}
		
		override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
			this.beginConfigureAnimation()
			this.setPosition(event)
			this.zoomBar.bindBehavior.requestDispatchEvent()
			this.endConfigureAnimation()
			return true
		}
		
		private fun beginConfigureAnimation()
		{
			this.positionSource = this.zoomBar.positionBehavior.getPosition()
		}
		
		private fun endConfigureAnimation()
		{
			this.positionDestination = this.zoomBar.positionBehavior.getPosition()
			this.positionAnimator.setFloatValues(
				this.positionSource,
				this.positionDestination
			)
			this.positionAnimator.start()
		}
		
		private fun setPosition(event: MotionEvent)
		{
			val limit = this.zoomBar.boundBehavior.getLimit()
			
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
				position < this.zoomBar.boundBehavior.getPaddingStart() -> this.zoomBar.boundBehavior.getPaddingStart().toFloat()
				position > limit -> limit
				else -> position
			}
			
			this.zoomBar.positionBehavior.setPosition(position)
		}
	}
	
	private val focusListener: FocusListener = FocusListener(this)
	private val gestureListener: GestureListener = GestureListener(this)
	private val gestureDetector: GestureDetector = GestureDetector(
		this.context,
		this.gestureListener
	)
	private val bindBehavior = BindBehavior<Zoomable>(this)
	private val boundBehavior = ZoomBarBoundBehavior(this)
	private val positionBehavior = StepPositionBehavior()
	private val factorBehavior = PropertyBehavior(0f)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val thumbRoundness: Float = this.resources.getDimension(
		R.dimen.zoombar_thumb_roundness
	).dpToPixel()
	
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
	private var _factor: Float = -1f
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
			this.boundBehavior.orientation = this._orientation
			this.invalidate()
		}
	
	var factor: Float
		get() = this._factor
		set(value) {
			val factor = MathUtils.nearest(
				value,
				this.factors
			)
//			this._factor = factor
			this.factorBehavior.setValue(this._factor)
		}
	
	var moveDuration: Long
		get() = this._moveDuration
		set(value) {
			this._moveDuration = value
			this.gestureListener.positionAnimator.duration = value
		}
	
	var focusEnterDuration: Long
		get() = this._focusEnterDuration
		set(value) {
			this._focusEnterDuration = value
			this.focusListener.focusEnterAnimator.startDelay = value
			this.focusListener.focusEnterAnimator.duration = value
		}
	
	var focusExitDuration: Long
		get() = this._focusExitDuration
		set(value) {
			this._focusExitDuration = value
			this.focusListener.focusExitAnimator.startDelay = value
			this.focusListener.focusExitAnimator.duration = value
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
		
		this._orientation = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_orientation,
			ViewOrientation.Horizontal.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		val factorTypedValue = TypedValue()
		this.resources.getValue(
			R.dimen.zoombar_factor,
			factorTypedValue,
			true
		)
		this._factor = typedArray.getFloat(
			R.styleable.ZoomBar_cio_zb_factor,
			factorTypedValue.float
		)
		
		this._moveDuration = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_moveDuration,
			this.resources.getInteger(R.integer.zoombar_move_duration)
		).toLong()
		
		this._focusEnterDuration = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_focusEnterDuration,
			this.resources.getInteger(R.integer.zoombar_focus_enter_duration)
		).toLong()
		
		this._focusExitDuration = typedArray.getInteger(
			R.styleable.ZoomBar_cio_zb_focusExitDuration,
			this.resources.getInteger(R.integer.zoombar_focus_exit_duration)
		).toLong()
		
		this._trackColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_trackColor,
			this.resources.getColor(R.color.borderColorPrimary, theme)
		)
		
		this._ticksColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_ticksColor,
			this.resources.getColor(R.color.borderColorPrimary, theme)
		)
		
		this._thumbColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_thumbColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this._bubbleBackgroundColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_bubbleBackgroundColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this._bubbleTextColor = typedArray.getColor(
			R.styleable.ZoomBar_cio_zb_bubbleTextColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._trackThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_trackThickness,
			this.resources.getDimension(R.dimen.zoombar_track_thickness)
		)
		
		this._ticksThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_ticksThickness,
			this.resources.getDimension(R.dimen.zoombar_ticks_thickness)
		)
		
		this._ticksPadding = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_ticksPadding,
			this.resources.getDimension(R.dimen.zoombar_ticks_padding)
		)
		
		this._thumbThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_thumbThickness,
			this.resources.getDimension(R.dimen.zoombar_thumb_thickness)
		)
		
		this._bubbleThickness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleThickness,
			this.resources.getDimension(R.dimen.zoombar_bubble_thickness)
		)
		
		this._bubbleRoundness = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleRoundness,
			this.resources.getDimension(R.dimen.zoombar_bubble_roundness)
		)
		
		this._bubblePadding = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubblePadding,
			this.resources.getDimension(R.dimen.zoombar_bubble_padding)
		)
		
		this._bubbleMargin = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleMargin,
			this.resources.getDimension(R.dimen.zoombar_bubble_margin)
		)
		
		this._bubbleTextSize = typedArray.getDimension(
			R.styleable.ZoomBar_cio_zb_bubbleTextSize,
			this.resources.getDimension(R.dimen.zoombar_bubble_text_size)
		)
		
		this._bubbleInvert = typedArray.getBoolean(
			R.styleable.ZoomBar_cio_zb_bubbleInvert,
			false
		)
		
		typedArray.recycle()
		
		this.isFocusable = true
		this.isFocusableInTouchMode = true
		this.onFocusChangeListener = this.focusListener
		
		this.focusEnterDuration = this._focusEnterDuration
		this.focusExitDuration = this._focusExitDuration
		this.focusListener.setOpacity(0f)
		
		this.bindBehavior.onAttach = {}
		this.bindBehavior.onDispatchEvent = {
			it.setZoomFactor(this.orientation, this.factor, !this.gestureListener.isScrolling)
		}
		
		this.boundBehavior.orientation = this.orientation
		
		this.positionBehavior.onMeasureSteps = this.boundBehavior::getSteps
		this.positionBehavior.onPositionChanged = { _, index ->
			this._factor = this.factors[index]
		}
		
		this.factorBehavior.onValueChanged = { value ->
			val index = this.factors.indexOf(value)
			this.positionBehavior.setIndex(index)
		}
		this.factorBehavior.setValue(this._factor)
	}
	
	override fun attach(id: Int)
	{
		this.bindBehavior.attach(id)
	}
	
	override fun attach(view: View)
	{
		this.bindBehavior.attach(view)
	}
	
	override fun attach(fragment: Fragment)
	{
		this.bindBehavior.attach(fragment)
	}
	
	override fun attach(activity: FragmentActivity)
	{
		this.bindBehavior.attach(activity)
	}
	
	override fun attachAll(views: List<View>)
	{
		this.bindBehavior.attachAll(views)
	}
	
	override fun detach(id: Int)
	{
		this.bindBehavior.detach(id)
	}
	
	override fun detach(view: View)
	{
		this.bindBehavior.detach(view)
	}
	
	override fun detachAll()
	{
		this.bindBehavior.detachAll()
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
	
	override fun onDraw(canvas: Canvas)
	{
		if(this.positionBehavior.getPosition().isNaN())
		{
			this.factorBehavior.setValue(this._factor)
		}
		
		this.drawTicks(canvas)
		this.drawTrack(canvas)
		this.drawThumb(canvas)
		this.drawBubble(canvas)
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
		
		val steps = this.boundBehavior.getSteps()
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
		val position = this.positionBehavior.getPosition()
		val roundness = this.thumbRoundness

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
		val position = this.positionBehavior.getPosition() + this.thumbThickness / 2
		val label = this.factor.toString()
		val width = (label.getTextBounds(this.painter).width() + this.bubblePadding).toInt()
		val halfWidth = width / 2f
		val height = this.bubbleThickness.toInt()
		val halfHeight = height / 2f
		val textHeight = label.getTextBounds(this.painter).height()

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

			val textPosition = label.alignCenter(
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
			
			val textPosition = label.alignCenter(
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