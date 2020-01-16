package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import io.chord.R
import io.chord.ui.utils.MathUtils
import kotlin.math.max
import kotlin.math.min

class ScrollBar : View
{
	private val painter: Paint = Paint()
	private val scrollBarControllers: MutableMap<Int, ScrollBarController> = mutableMapOf()
	
	private var _orientation: ViewOrientation = ViewOrientation.Horizontal
	private var _trackColor: Int = -1
	private var _thumbColor: Int = -1
	private var _trackThickness: Float = -1f
	private var _thumbThickness: Float = -1f
	private var _thumbRoundness: Float = -1f
	
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
	
	var thumbRoundness: Float
		get() = this._thumbRoundness
		set(value) {
			this._thumbRoundness = value
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
			attrs, R.styleable.ScrollBar, defStyle, 0
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
		
		this.thumbColor = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_thumbColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.trackThickness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_trackThickness,
			this.resources.getDimension(R.dimen.scrollbar_track_thickness)
		)
		
		this.thumbThickness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_thumbThickness,
			this.resources.getDimension(R.dimen.scrollbar_thumb_thickness)
		)
		
		this.thumbRoundness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_thumbRoundness,
			this.resources.getDimension(R.dimen.scrollbar_thumb_roundness)
		)
		
		typedArray.recycle()
	}
	
	fun attach(id: Int)
	{
		this.scrollBarControllers[id] = ScrollBarController(id, this)
	}
	
	fun detach(id: Int)
	{
		this.scrollBarControllers.remove(id)
	}
	
	private fun checkScrollBarControllerAreEquals()
	{
		if(this.scrollBarControllers.isEmpty() || this.scrollBarControllers.size == 1)
		{
			return
		}
		
		val result = this.scrollBarControllers
			.values
			.stream()
			.distinct()
		
		if(result.count() > 1)
		{
			throw java.lang.IllegalStateException("all scrollviews must have the same dimension, including content and padding")
		}
	}
	
	private fun getScrollViewContentSize(): Int
	{
		this.checkScrollBarControllerAreEquals()
		return this.scrollBarControllers.values.first().getContentSize()
	}
	
	private fun getScrollViewSize(): Int
	{
		this.checkScrollBarControllerAreEquals()
		return this.scrollBarControllers.values.first().getSize()
	}
	
	private fun getScrollViewPosition(): Int
	{
		this.checkScrollBarControllerAreEquals()
		return this.scrollBarControllers.values.first().getPosition()
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
	
	private fun getPadding(): Int
	{
		return if(this.orientation == ViewOrientation.Vertical)
		{
			this.paddingTop + this.paddingBottom
		}
		else
		{
			this.paddingStart + this.paddingEnd
		}
	}
	
	private fun getSizeFromContent(): Float
	{
		val contentSize = this.getScrollViewContentSize().toFloat()
		val scrollViewSize = this.getScrollViewSize().toFloat()
		val scrollBarSize = this.getSizeWithoutPaddings()
		val maxSize = max(scrollViewSize, contentSize)
		val minSize= min(scrollViewSize, contentSize)
		val ratio = minSize / maxSize
		return ratio * scrollBarSize
	}
	
	private fun getPosition(): Float
	{
		val contentSize = this.getScrollViewContentSize().toFloat()
		val scrollBarSize = this.getSizeWithoutPaddings().toFloat()
		val position = this.getScrollViewPosition().toFloat()
		return MathUtils.map(
			position,
			0f,
			contentSize,
			0f,
			scrollBarSize
		)
	}
	
	private fun setPosition(position: Int)
	{
		this.checkScrollBarControllerAreEquals()
		this.scrollBarControllers.forEach {
			it.value.setPosition(position)
		}
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		val position = if(this.orientation == ViewOrientation.Vertical)
		{
			event.y
		}
		else
		{
			event.x
		}
		
		val scrollBarSize = this.getSizeWithoutPaddings().toFloat()
		val scrollContentSize = this.getScrollViewContentSize().toFloat()
		val scrollViewSize = this.getScrollViewSize().toFloat()
		val scaledPosition = MathUtils.map(
			position,
			0f,
			scrollBarSize,
			0f,
			scrollContentSize - scrollViewSize
		)
		
		this.setPosition(scaledPosition.toInt())
		
		return true
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		this.drawTrack(canvas)
		
		if(this.scrollBarControllers.isNotEmpty())
		{
			this.drawThumb(canvas)
		}
		
		this.invalidate()
	}
	
	private fun drawTrack(canvas: Canvas?)
	{
		this.painter.color = this.trackColor
		this.painter.strokeWidth = this.trackThickness
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
		this.painter.color = this.thumbColor
		
		val thickness = this.thumbThickness
		val roundness = this.thumbRoundness
		val position = this.getPosition()
		val size = this.getSizeFromContent()
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			val centerVertical = (this.height / 2f) - (thickness / 2f)
			val left = position + this.paddingStart
			val right = position + size - this.paddingEnd
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
			val bottom = position + size - this.paddingBottom
			canvas?.drawRoundRect(
				centerHorizontal,
				top,
				centerHorizontal + thickness,
				bottom ,
				roundness,
				roundness,
				this.painter
			)
		}
	}
}