package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import io.chord.R
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.KeyboardKeyBehavior
import io.chord.ui.behaviors.SurfaceGestureBehavior
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.extensions.alignCenter
import io.chord.ui.extensions.alignRight
import io.chord.ui.extensions.findOptimalTextSize
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
					it.isSelected && it is KeyboardKeyBehavior.BlackKeyTouchSurface
				}
				
				if(isBlackKey)
				{
					this.gesture.surfaces.filterIsInstance(
						KeyboardKeyBehavior.WhiteKeyTouchSurface::class.java
					).forEach {
						it.isSelected = false
					}
				}
				
				this.keyboard.postInvalidate()
				return true
			}
			return false
		}
	}
	
	private val zoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val gestureBehavior = SurfaceGestureBehavior(this.context)
	private val keyBehavior = KeyboardKeyBehavior()
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private val halfStrokeThickness: Float get() = this._strokeThickness / 2f
	private var text: String = ""
	private var textSize: Float = 0f
	
	private var _orientation: ViewOrientation = ViewOrientation.Horizontal
	private var _zoomDuration: Long = -1
	private var _whiteKeyColor: Int = -1
	private var _blackKeyColor: Int = -1
	private var _strokeColor: Int = -1
	private var _touchColor: Int = -1
	private var _textColor: Int = -1
	private var _strokeThickness: Float = 0f
	private var _textWeight: Float = -1f
	private var _keyWieght: Float = -1f
	private var _textMargin: Float = -1f
	private var _octave: Int = 0
	private var _showOctave: Boolean = false
	private var _disableTouchEvent: Boolean = false
	private var _clampOutsideStroke: Boolean = false
	
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
	
	var touchColor: Int
		get() = this._touchColor
		set(value) {
			this._touchColor = value
			this.invalidate()
		}
	
	var textColor: Int
		get() = this._textColor
		set(value) {
			this._textColor = value
			this.invalidate()
		}
	
	var strokeThickness: Float
		get() = this._strokeThickness
		set(value) {
			this._strokeThickness = value
			this.requestLayout()
			this.invalidate()
		}
	
	var textWeight: Float
		get() = this._textWeight
		set(value) {
			this._textWeight = when
			{
				value > 1f -> 1f
				value < 0f -> 0f
				else -> value
			}
			this.requestLayout()
			this.invalidate()
		}
	
	var keyWeight: Float
		get() = this._keyWieght
		set(value) {
			this._keyWieght = when
			{
				value > 1f -> 1f
				value < 0f -> 0f
				else -> value
			}
			this.requestLayout()
			this.invalidate()
		}
	
	var textMargin: Float
		get() = this._textMargin
		set(value) {
			this._textMargin = value
			this.requestLayout()
			this.invalidate()
		}
	
	var octave: Int
		get() = this._octave
		set(value) {
			this._octave = value
			this.requestLayout()
			this.invalidate()
		}
	
	var showOctave: Boolean
		get() = this._showOctave
		set(value) {
			this._showOctave = value
			this.invalidate()
		}
	
	var disableTouchEvent: Boolean
		get() = this._disableTouchEvent
		set(value) {
			this._disableTouchEvent = value
		}
	
	var clampOutsideStroke: Boolean
		get() = this._clampOutsideStroke
		set(value) {
			this._clampOutsideStroke = value
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
		
		this._touchColor = typedArray.getColor(
			R.styleable.Keyboard_cio_kb_touchColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this._textColor = typedArray.getColor(
			R.styleable.Keyboard_cio_kb_textColor,
			this.resources.getColor(R.color.borderColorTernary, theme)
		)
		
		this._strokeThickness = typedArray.getDimension(
			R.styleable.Keyboard_cio_kb_strokeThickness,
			this.resources.getDimension(R.dimen.keyboard_stroke_thickness)
		)
		
		this._textWeight = typedArray.getFloat(
			R.styleable.Keyboard_cio_kb_textWeight,
			this.resources.getInteger(R.integer.keyboard_text_weight) / 100f
		)
		
		this._keyWieght = typedArray.getFloat(
			R.styleable.Keyboard_cio_kb_keyWeight,
			this.resources.getInteger(R.integer.keyboard_key_weight) / 100f
		)
		
		this._textMargin = typedArray.getDimension(
			R.styleable.Keyboard_cio_kb_textMargin,
			this.resources.getDimension(R.dimen.keyboard_text_margin)
		)
		
		this._octave = typedArray.getInteger(
			R.styleable.Keyboard_cio_kb_octave,
			this.resources.getInteger(R.integer.keyboard_octave)
		)
		
		this._showOctave = typedArray.getBoolean(
			R.styleable.Keyboard_cio_kb_showOctave,
			this.resources.getBoolean(R.bool.keyboard_show_octave)
		)
		
		this._disableTouchEvent = typedArray.getBoolean(
			R.styleable.Keyboard_cio_kb_disableTouchEvent,
			this.resources.getBoolean(R.bool.keyboard_disable_touch_event)
		)
		
		this._clampOutsideStroke = typedArray.getBoolean(
			R.styleable.Keyboard_cio_kb_clampOutsideStroke,
			this.resources.getBoolean(R.bool.keyboard_clamp_outside_stroke)
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
		
		this.setZoomFactor(this.orientation, 1f, false)
		
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
		return if(!this.disableTouchEvent && this.gestureBehavior.onTouchEvent(event))
		{
			true
		}
		else
		{
			this.clearFocus()
			super.onTouchEvent(event)
		}
	}
	
	private fun findOptimalTextSize(constraint: Float, boundsEvaluator: ((Rect) -> Int)): Float
	{
		val sizes = mutableListOf<Float>()
		for(index in 0..9)
		{
			sizes.add("C$index".findOptimalTextSize(
				0f,
				constraint,
				boundsEvaluator,
				this.painter
			))
		}
		
		return sizes.min()!!
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
	{
		this.zoomBehavior.requestMeasure()
		
		this.text = "C${this.octave}"
		val stroke = this.strokeThickness
		val clampedStroke = when
		{
			this.clampOutsideStroke -> 0f
			else -> stroke * 2f
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
				stroke,
				this.clampOutsideStroke
			)
			measuredWidth = ((this.keyBehavior.white.width * 7f) + (stroke * 6f) + clampedStroke).toInt()
			measuredHeight = (this.keyBehavior.white.height + clampedStroke).toInt()
			this.setMeasuredDimension(
				measuredWidth,
				measuredHeight
			)
			this.textSize = this.findOptimalTextSize(
				this.keyBehavior.white.width * this.textWeight,
				{bounds -> bounds.width()}
			)
		}
		else
		{
			this.keyBehavior.measure(
				this.orientation,
				this.layoutParams,
				widthMeasureSpec,
				this.zoomBehavior.factorizedHeight,
				stroke,
				this.clampOutsideStroke
			)
			measuredWidth = (this.keyBehavior.white.width + clampedStroke).toInt()
			measuredHeight = ((this.keyBehavior.white.height * 7f) + (stroke * 6f) + clampedStroke).toInt()
			this.setMeasuredDimension(
				measuredWidth,
				measuredHeight
			)
			this.textSize = this.findOptimalTextSize(
				this.keyBehavior.white.height * this.textWeight,
				{bounds -> bounds.height()}
			)
		}
		this.gestureBehavior.bounds = this.getAbsoluteClipBounds(measuredWidth, measuredHeight)
		this.gestureBehavior.surfaces.clear()
	}
	
	override fun onDraw(canvas: Canvas)
	{
		this.painter.color = this.strokeColor
		this.painter.style = Paint.Style.FILL
		canvas.drawRect(canvas.clipBounds, this.painter)
		
		this.drawWhiteKeys(canvas)
		this.drawBlackKeys(canvas)
	}
	
	private fun drawWhiteKeys(canvas: Canvas)
	{
		this.keyBehavior.white.draw(
			canvas,
			this.painter,
			this.gestureBehavior.surfaces,
			this.orientation,
			this.strokeThickness,
			1f,
			this.clampOutsideStroke,
			this.whiteKeyColor,
			this.strokeColor,
			this.touchColor
		) {rectangle, index ->
			if(this.orientation == ViewOrientation.Horizontal && index == 0 && this.showOctave)
			{
				this.painter.textSize = this.textSize
				this.painter.color = this.textColor
				this.painter.strokeWidth = 0f
				val horizontalPosition = this.text.alignCenter(
					rectangle.centerX().toInt(), 0, this.painter
				)
				val verticalPosition = this.text.alignRight(
					0, (rectangle.bottom).toInt(), this.painter
				)
				canvas.drawText(
					this.text,
					0,
					this.text.length,
					horizontalPosition.x,
					verticalPosition.y - this.textMargin,
					this.painter
				)
			}
			else if(this.orientation == ViewOrientation.Vertical && index == 0 && this.showOctave)
			{
				this.painter.textSize = this.textSize
				this.painter.color = this.textColor
				this.painter.strokeWidth = 0f
				val horizontalPosition = this.text.alignRight(
					(rectangle.right).toInt(), 0, this.painter
				)
				val verticalPosition = this.text.alignCenter(
					0, rectangle.centerY().toInt(), this.painter
				)
				canvas.drawText(
					this.text,
					0,
					this.text.length,
					horizontalPosition.x - this.textMargin,
					verticalPosition.y,
					this.painter
				)
			}
		}
	}
	
	private fun drawBlackKeys(canvas: Canvas)
	{
		this.keyBehavior.black.draw(
			canvas,
			this.painter,
			this.gestureBehavior.surfaces,
			this.orientation,
			this.strokeThickness,
			this.keyWeight,
			this.clampOutsideStroke,
			this.blackKeyColor,
			this.strokeColor,
			this.touchColor,
			null
		)
	}
}