package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import io.chord.R
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.KeyboardKeyBehavior
import io.chord.ui.behaviors.SurfaceGestureBehavior
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.extensions.addIfNotPresent
import io.chord.ui.extensions.getAbsoluteClipBounds
import io.chord.ui.gestures.SimpleOnGestureListener
import kotlinx.coroutines.Runnable

class Keyboard : View, Zoomable
{
	private class GestureListener(
		private val keyboard: Keyboard,
		private val gesture: SurfaceGestureBehavior
	): SimpleOnGestureListener()
	{
		private var runnable: Runnable? = null
		
		override fun onDown(event: MotionEvent): Boolean
		{
			return this.select(event)
		}
		
		override fun onUp(event: MotionEvent): Boolean
		{
			this.keyboard.removeCallbacks(this.runnable)
			this.runnable = kotlinx.coroutines.Runnable {
				this.keyboard.clearFocus()
			}
			this.keyboard.postDelayed(this.runnable, 1000)
			return true
		}
		
		override fun onMove(event: MotionEvent): Boolean
		{
			return this.select(event)
		}
		
		private fun select(event: MotionEvent): Boolean
		{
			this.gesture.contains(event.x, event.y)
			if(this.gesture.surfaces.any { it.isSelected })
			{
				val isBlackKey = this.gesture.surfaces.any {
					it.isSelected && it is BlackKeyTouchSurface
				}
				
				if(isBlackKey)
				{
					this.gesture.surfaces.filterIsInstance(WhiteKeyTouchSurface::class.java).forEach {
						it.isSelected = false
					}
				}
				
				this.keyboard.postInvalidate()
				return true
			}
			return false
		}
	}
	
	private class WhiteKeyTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	{
		override fun equals(other: Any?): Boolean
		{
			if(other is WhiteKeyTouchSurface)
			{
				val isRectangleEqual = this.rectangle == other.rectangle
				val isIndexEqual = this.index == other.index
				return isRectangleEqual && isIndexEqual
			}
			
			return false
		}
		
		override fun hashCode(): Int
		{
			return super.hashCode()
		}
	}
	
	private class BlackKeyTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	{
		override fun equals(other: Any?): Boolean
		{
			if(other is BlackKeyTouchSurface)
			{
				val isRectangleEqual = this.rectangle == other.rectangle
				val isIndexEqual = this.index == other.index
				return isRectangleEqual && isIndexEqual
			}
			
			return false
		}
		
		override fun hashCode(): Int
		{
			return super.hashCode()
		}
		
	}
	
	private val zoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val gestureBehavior = SurfaceGestureBehavior(this.context)
	private val keyBehavior = KeyboardKeyBehavior()
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private val halfStrokeThickness: Float
		get() = this._strokeThickness / 2f
	
	var clampOutsideStroke: Boolean = false
		set(value) {
			field = value
			this.requestLayout()
			this.invalidate()
		}
	
	var clampOutsideLeftStroke: Boolean = false
		set(value) {
			field = value
			this.requestLayout()
			this.invalidate()
		}
	
	var clampOutsideRightStroke: Boolean = false
		set(value) {
			field = value
			this.requestLayout()
			this.invalidate()
		}
	
	val bounds: Rect
		get() = this.gestureBehavior.bounds
	
	private var _orientation: ViewOrientation = ViewOrientation.Horizontal
	private var _zoomDuration: Long = -1
	private var _whiteKeyColor: Int = -1
	private var _blackKeyColor: Int = -1
	private var _strokeColor: Int = -1
	private var _strokeThickness: Float = 0f
	
	var orientation: ViewOrientation
		get() = this._orientation
		set(value) {
			this._orientation = value
			this.setZoomFactor(
				this.orientation,
				when(this.orientation)
				{
					ViewOrientation.Horizontal -> this.zoomBehavior.getFactorWidth()
					else -> this.zoomBehavior.getFactorHeight()
				},
				true
			)
			this.requestLayout()
			this.invalidate()
		}
	
	var zoomDuration: Long
		get() = this._zoomDuration
		set(value) {
			this._zoomDuration = value
			this.zoomBehavior.widthAnimator.duration = value
			this.zoomBehavior.heightAnimator.duration = value
		}
	
	var whiteKeyColor: Int
		get() = this._whiteKeyColor
		set(value) {
			this._whiteKeyColor = value
			this.invalidate()
		}
	
	var blackKeyColor: Int
		get() = this._blackKeyColor
		set(value) {
			this._blackKeyColor = value
			this.invalidate()
		}
	
	var strokeColor: Int
		get() = this._strokeColor
		set(value) {
			this._strokeColor = value
			this.invalidate()
		}
	
	var strokeThickness: Float
		get() = this._strokeThickness
		set(value) {
			this._strokeThickness = value
			this.requestLayout()
			this.invalidate()
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
			attrs, R.styleable.Keyboard, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._orientation = typedArray.getInteger(
			R.styleable.Keyboard_cio_kb_orientation,
			ViewOrientation.Horizontal.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		this._zoomDuration = typedArray.getInteger(
			R.styleable.Keyboard_cio_kb_zoomDuration,
			this.resources.getInteger(R.integer.keyboard_zoom_duration)
		).toLong()
		
		this._whiteKeyColor = typedArray.getColor(
			R.styleable.Keyboard_cio_kb_whiteKeyColor,
			this.resources.getColor(R.color.textColorSecondary, theme)
		)
		
		this._blackKeyColor = typedArray.getColor(
			R.styleable.Keyboard_cio_kb_blackKeyColor,
			this.resources.getColor(R.color.backgroundSecondary, theme)
		)
		
		this._strokeColor = typedArray.getColor(
			R.styleable.Keyboard_cio_kb_strokeColor,
			this.resources.getColor(R.color.borderColorTernary, theme)
		)
		
		this._strokeThickness = typedArray.getDimension(
			R.styleable.Keyboard_cio_kb_strokeThickness,
			this.resources.getDimension(R.dimen.keyboard_stroke_thickness)
		)
		
		typedArray.recycle()
		
		this.zoomBehavior.widthAnimator.duration = this.zoomDuration
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		this.zoomBehavior.onEvaluateWidth = this::getKeyWidth
		this.zoomBehavior.onMeasureWidth = {
			this.requestLayout()
			this.invalidate()
		}
		this.zoomBehavior.onEvaluateHeight = this::getKeyWidth
		this.zoomBehavior.onMeasureHeight = {
			this.requestLayout()
			this.invalidate()
		}
		
		this.gestureBehavior.setListener(GestureListener(this, this.gestureBehavior))
		
		this.isClickable = true
		this.isFocusable = true
	}
	
	override fun attach(controller: BindBehavior<Bindable>)
	{
		this.bindableBehavior.attach(controller)
	}
	
	override fun selfAttach()
	{
		this.bindableBehavior.selfAttach()
	}
	
	override fun selfDetach()
	{
		this.bindableBehavior.selfDetach()
	}
	
	override fun setZoomFactor(orientation: ViewOrientation, factor: Float, animate: Boolean)
	{
		if(orientation == ViewOrientation.Horizontal)
		{
			this.zoomBehavior.setFactorWidth(factor, animate)
		}
		else
		{
			this.zoomBehavior.setFactorHeight(factor, animate)
		}
	}
	
	private fun getKeyWidth(): Float
	{
		if(this.layoutParams == null)
		{
			return 0f
		}
		return this.keyBehavior.getKeyWidth(this.orientation, this.layoutParams)
	}
	
	fun contains(x: Int, y: Int): Boolean
	{
		return this.gestureBehavior.bounds.contains(x, y)
	}
	
	override fun clearFocus()
	{
		super.clearFocus()
		this.gestureBehavior.surfaces.forEach {
			it.isSelected = false
		}
		this.invalidate()
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		return if(this.gestureBehavior.onTouchEvent(event))
		{
			true
		}
		else
		{
			this.clearFocus()
			super.onTouchEvent(event)
		}
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
	{
		val stroke = this.strokeThickness
		val clampedStroke = if(this.clampOutsideLeftStroke)
		{
			-stroke
		}
		else if(this.clampOutsideRightStroke)
		{
			0f
		}
		else if(this.clampOutsideStroke)
		{
			0f
		}
		else
		{
			stroke
		}
		val measuredWidth: Int
		val measuredHeight: Int
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			this.keyBehavior.measure(
				this.orientation,
				this.layoutParams,
				this.zoomBehavior.factorizedWidth,
				heightMeasureSpec,
				stroke
			)
			measuredWidth = (this.keyBehavior.white.width * 7f + clampedStroke).toInt()
			measuredHeight = (this.keyBehavior.white.height + stroke).toInt()
			this.setMeasuredDimension(
				measuredWidth,
				measuredHeight
			)
		}
		else
		{
			this.keyBehavior.measure(
				this.orientation,
				this.layoutParams,
				widthMeasureSpec,
				this.zoomBehavior.factorizedHeight,
				stroke
			)
			measuredWidth = (this.keyBehavior.white.width + stroke).toInt()
			measuredHeight = (this.keyBehavior.white.height * 7f + clampedStroke).toInt()
			this.setMeasuredDimension(
				measuredWidth,
				measuredHeight
			)
		}
		this.gestureBehavior.bounds = this.getAbsoluteClipBounds(measuredWidth, measuredHeight)
		this.gestureBehavior.surfaces.clear()
	}
	
	override fun onDraw(canvas: Canvas)
	{
		this.drawWhiteKeys(canvas)
		this.drawBlackKeys(canvas)
	}
	
	private fun drawWhiteKeys(canvas: Canvas)
	{
		val surfaces = this.gestureBehavior.surfaces.filterIsInstance(
			WhiteKeyTouchSurface::class.java
		)
		val bounds = canvas.clipBounds.toRectF()
		
		this.painter.strokeWidth = this.strokeThickness
		
		for(index in 0 until 7)
		{
			val surface = surfaces.firstOrNull {
				it.index == index
			}
			val rect = this.keyBehavior.white.translate(
				this.orientation,
				index,
				bounds,
				this.strokeThickness,
				this.clampOutsideLeftStroke
			)
			
			if(surface != null && surface.isSelected)
			{
				this.painter.color = this.resources.getColor(android.R.color.holo_red_light)
				this.painter.style = Paint.Style.FILL
			}
			else
			{
				this.gestureBehavior.surfaces.addIfNotPresent(WhiteKeyTouchSurface(
					rect,
					index
				))
				
				this.painter.color = this.whiteKeyColor
				this.painter.style = Paint.Style.FILL
			}
			
			canvas.drawRect(rect, this.painter)
			
			this.painter.color = this.strokeColor
			this.painter.style = Paint.Style.STROKE
			canvas.drawRect(rect, this.painter)
		}
	}
	
	private fun drawBlackKeys(canvas: Canvas)
	{
		val surfaces = this.gestureBehavior.surfaces.filterIsInstance(
			BlackKeyTouchSurface::class.java
		)
		val bounds = canvas.clipBounds.toRectF()
		
		this.painter.strokeWidth = this.strokeThickness
		
		for(index in 0 until 6)
		{
			if((this.orientation == ViewOrientation.Horizontal && index == 2) || (this.orientation == ViewOrientation.Vertical && index == 3))
			{
				continue
			}
			
			val surface = surfaces.firstOrNull {
				it.index == index
			}
			val rect = this.keyBehavior.black.translate(
				this.orientation,
				index,
				bounds,
				this.strokeThickness,
				this.clampOutsideLeftStroke
			)
			
			if(this.orientation == ViewOrientation.Horizontal)
			{
				rect.set(
					rect.left,
					rect.top,
					rect.right,
					rect.bottom * 0.6f
				)
			}
			else
			{
				rect.set(
					rect.left,
					rect.top,
					rect.right * 0.6f,
					rect.bottom
				)
			}
			
			if(surface != null && surface.isSelected)
			{
				this.painter.color = this.resources.getColor(android.R.color.holo_red_light)
				this.painter.style = Paint.Style.FILL
				canvas.drawRect(rect, this.painter)
			}
			else
			{
				this.gestureBehavior.surfaces.addIfNotPresent(BlackKeyTouchSurface(
					rect,
					index
				))
				
				this.painter.color = this.blackKeyColor
				this.painter.style = Paint.Style.FILL
				canvas.drawRect(rect, this.painter)
				
				this.painter.color = this.strokeColor
				this.painter.style = Paint.Style.STROKE
				canvas.drawRect(rect, this.painter)
			}
		}
	}
}