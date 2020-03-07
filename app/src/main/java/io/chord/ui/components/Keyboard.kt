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
import io.chord.ui.behaviors.SurfaceGestureBehavior
import io.chord.ui.behaviors.ZoomBehavior
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
			this.select(event)
			return true
		}
		
		override fun onUp(event: MotionEvent): Boolean
		{
			this.keyboard.removeCallbacks(this.runnable)
			this.runnable = kotlinx.coroutines.Runnable {
				this.unselect()
			}
			this.keyboard.postDelayed(this.runnable, 1000)
			return true
		}
		
		override fun onMove(event: MotionEvent): Boolean
		{
			this.select(event)
			return true
		}
		
		private fun select(event: MotionEvent)
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
			}
		}
		
		private fun unselect()
		{
			this.gesture.surfaces.forEach {
				it.isSelected = false
			}
			this.keyboard.invalidate()
		}
	}
	
	private class WhiteKeyTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	
	private class BlackKeyTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	
	private abstract class Key(
		protected val keyboard: Keyboard
	)
	{
		// h.r = w
		// w/r = h
		// w/h = r
		// wk.R = 0.28
		// bk.R = 0.22
		
		protected var _width: Float = 0f
		protected var _height: Float = 0f
		
		val width: Float
			get() = this._width
		
		val height: Float
			get() = this._height
		
		abstract fun setBounds(width: Float, height: Float, stroke: Float)
		
		abstract fun translate(index: Int, bounds: RectF): RectF
	}
	
	private open class WhiteKey(
		keyboard: Keyboard
	) : Key(keyboard)
	{
		override fun setBounds(width: Float, height: Float, stroke: Float)
		{
			this._width = width - stroke
			this._height = height - stroke
		}
		
		override fun translate(index: Int, bounds: RectF): RectF
		{
			// TODO: refactor components to use private class
			// TODO: refactor codes to look like this
			
			val rect = RectF()
			val halfStroke = this.keyboard.halfStrokeThickness
			val left: Float
			val right: Float
			val top: Float
			val bottom: Float
			
			if(this.keyboard.orientation == ViewOrientation.Horizontal)
			{
				left = this.width * index + bounds.left + halfStroke
				right = left + this.width
				top = bounds.top + halfStroke
				bottom = top + this.height - halfStroke
			}
			else
			{
				left = bounds.left + halfStroke
				right = left + this.width - halfStroke
				top = this.height * index + bounds.top + halfStroke
				bottom = top + this.height
			}
			
			rect.set(
				left,
				top,
				right,
				bottom
			)
			
			return rect
		}
	}
	
	private class BlackKey(
		keyboard: Keyboard
	) : WhiteKey(keyboard)
	{
		private var whiteKey = WhiteKey(keyboard)
		
		override fun setBounds(width: Float, height: Float, stroke: Float)
		{
			this.whiteKey.setBounds(width, height, stroke)
			
			if(this.keyboard.orientation == ViewOrientation.Horizontal)
			{
				this._width = this.whiteKey.width / 2f
				this._height = this.whiteKey.height
			}
			else
			{
				this._width = this.whiteKey.width
				this._height = this.whiteKey.height / 2f
			}
		}
		
		override fun translate(index: Int, bounds: RectF): RectF
		{
			val parent =  this.whiteKey.translate(index, bounds)
			val rect = super.translate(index, bounds)
			
			if(this.keyboard.orientation == ViewOrientation.Horizontal)
			{
				val left = parent.left + (this.width * 1.5f)
				val right = left + this.width
				
				rect.set(
					left,
					rect.top,
					right,
					rect.bottom
				)
			}
			else
			{
				val top = parent.top + (this.height * 1.5f)
				val bottom = top + this.height
				
				rect.set(
					rect.left,
					top,
					rect.right,
					bottom
				)
			}
			
			return rect
		}
	}
	
	private val zoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val gestureBehavior = SurfaceGestureBehavior(this.context)
	
	private val bounds = Rect()
	private val whiteKey = WhiteKey(this)
	private val blackKey = BlackKey(this)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private val halfStrokeThickness: Float
		get() = this._strokeThickness / 2f
	
	private var _orientation: ViewOrientation = ViewOrientation.Horizontal
	private var _zoomDuration: Long = -1
	private var _whiteKeyColor: Int = -1
	private var _blackKeyColor: Int = -1
	private var _strokeColor: Int = -1
	private var _keyWidth: Float = -1f
	private var _keyHeight: Float = -1f
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
	
	var keyWidth: Float
		get() = this._keyWidth
		set(value) {
			this._keyWidth = value
			this.requestLayout()
			this.invalidate()
		}
	
	var keyHeight: Float
		get() = this._keyHeight
		set(value) {
			this._keyHeight = value
			this.requestLayout()
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
		
		this._keyWidth = typedArray.getDimension(
			R.styleable.Keyboard_cio_kb_keyWidth,
			this.resources.getDimension(R.dimen.keyboard_key_width)
		)
		
		this._keyHeight = typedArray.getDimension(
			R.styleable.Keyboard_cio_kb_keyHeight,
			this.resources.getDimension(R.dimen.keyboard_key_height)
		)
		
		this._strokeThickness = typedArray.getDimension(
			R.styleable.Keyboard_cio_kb_strokeThickness,
			this.resources.getDimension(R.dimen.keyboard_stroke_thickness)
		)
		
		typedArray.recycle()
		
		this.zoomBehavior.widthAnimator.duration = this.zoomDuration
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		this.zoomBehavior.onEvaluateWidth = this::keyWidth
		this.zoomBehavior.onMeasureWidth = {
			this.requestLayout()
			this.invalidate()
		}
		this.zoomBehavior.onEvaluateHeight = this::keyWidth
		this.zoomBehavior.onMeasureHeight = {
			this.requestLayout()
			this.invalidate()
		}
		
		this.gestureBehavior.setListener(GestureListener(this, this.gestureBehavior))
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
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		return this.gestureBehavior.onTouchEvent(event)
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
	{
		val stroke = this.strokeThickness
		val halfStroke = stroke / 2f
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			val width = this.zoomBehavior.factorizedWidth
			val height = this.keyHeight
			this.whiteKey.setBounds(width, height, stroke)
			this.blackKey.setBounds(width, height, stroke)
			this.setMeasuredDimension(
				(this.whiteKey.width * 7f + this.strokeThickness).toInt(),
				(this.whiteKey.height + halfStroke).toInt()
			)
		}
		else
		{
			val width = this.keyHeight
			val height = this.zoomBehavior.factorizedHeight
			this.whiteKey.setBounds(width, height, stroke)
			this.blackKey.setBounds(width, height, stroke)
			this.setMeasuredDimension(
				(this.whiteKey.width + halfStroke).toInt(),
				(this.whiteKey.height * 7f + this.strokeThickness).toInt()
			)
		}
		
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
		
		this.painter.strokeWidth = this.strokeThickness
		
		for(index in 0 until 7)
		{
			val surface = surfaces.firstOrNull {
				it.index == index
			}
			val rect = this.whiteKey.translate(index, canvas.clipBounds.toRectF())
			
			if(surface != null && surface.isSelected)
			{
				this.painter.color = this.resources.getColor(android.R.color.holo_red_light)
				this.painter.style = Paint.Style.FILL
			}
			else
			{
				this.gestureBehavior.surfaces.add(WhiteKeyTouchSurface(
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
			val rect = this.blackKey.translate(index, canvas.clipBounds.toRectF())
			
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
			}
			else
			{
				this.gestureBehavior.surfaces.add(BlackKeyTouchSurface(
					rect,
					index
				))
				
				this.painter.color = this.blackKeyColor
				this.painter.style = Paint.Style.FILL
			}
			
			canvas.drawRect(rect, this.painter)
			
			this.painter.color = this.strokeColor
			this.painter.style = Paint.Style.STROKE
			canvas.drawRect(rect, this.painter)
		}
	}
}