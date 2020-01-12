package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import io.chord.R
import io.chord.ui.utils.MathUtils
import io.chord.ui.utils.ViewUtils
import kotlin.math.max
import kotlin.math.min

class ScrollBar : View
{
	private val painter: Paint = Paint()
	private lateinit var _scrollView: FrameLayout
	
	private var _scrollviewId: Int? = null
	private var _orientation: ScrollBarOrientation = ScrollBarOrientation.Horizontal
	private var _backgroundColor: Int? = null
	private var _trackColor: Int? = null
	private var _color: Int? = null
	private var _trackThickness: Float? = null
	private var _thumbThickness: Float? = null
	private var _thumbRoundness: Float? = null
	
	var scrollviewId: Int?
		get() = this._scrollviewId
		set(value) {
			this._scrollviewId = value
		}
	
	var orientation: ScrollBarOrientation
		get() = this._orientation
		set(value) {
			this._orientation = value
		}
	
	var backgroundColor: Int?
		get() = this._backgroundColor
		set(value) {
			this._backgroundColor = value
		}
	
	var trackColor: Int?
		get() = this._trackColor
		set(value) {
			this._trackColor = value
		}
	
	var color: Int?
		get() = this._color
		set(value) {
			this._color = value
		}
	
	var trackThickness: Float?
		get() = this._trackThickness
		set(value) {
			this._trackThickness = value
		}
	
	var thumbThickness: Float?
		get() = this._thumbThickness
		set(value) {
			this._thumbThickness = value
		}
	
	var thumbRoundness: Float?
		get() = this._thumbRoundness
		set(value) {
			this._thumbRoundness = value
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
		
		this.scrollviewId = typedArray.getResourceId(
			R.styleable.ScrollBar_cio_sb_attachScrollView,
			0
		)
		
		typedArray.getString(
			R.styleable.ScrollBar_cio_sb_orientation
		)?.apply {
			orientation = if(this == ScrollBarOrientation.Vertical.orientation)
			{
				ScrollBarOrientation.Vertical
			} else
			{
				ScrollBarOrientation.Horizontal
			}
		}
		
		val theme = this.context.theme
		
		this.backgroundColor = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_backgroundColor,
			this.resources.getColor(R.color.backgroundSecondary, theme)
		)
		
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
	
	override fun onAttachedToWindow()
	{
		super.onAttachedToWindow()
		
		val rootView = ViewUtils.getParentRootView(this)
		this._scrollviewId?.apply {
			val twoDimensionalScrollView = rootView
				.findViewById<TwoDimensionalScrollView>(this)
			_scrollView = if(_orientation == ScrollBarOrientation.Vertical)
			{
				twoDimensionalScrollView.verticalScrollView
			}
			else
			{
				twoDimensionalScrollView.horizontalScrollView
			}
		}
	}
	
	private fun getScrollViewContentSize(): Int
	{
		if(this._scrollView.childCount > 0)
		{
			val child = this._scrollView.getChildAt(0)
			
			return if(this.orientation == ScrollBarOrientation.Vertical)
			{
				child.height
			}
			else
			{
				child.width
			}
		}
		
		return this.getScrollViewSize()
	}
	
	private fun getScrollViewSize(): Int
	{
		return if(this.orientation == ScrollBarOrientation.Vertical)
		{
			this._scrollView.height - this._scrollView.paddingBottom - this._scrollView.paddingTop
		}
		else
		{
			this._scrollView.width - this._scrollView.paddingStart - this._scrollView.paddingEnd
		}
	}
	
	private fun getScrollViewPosition(): Int
	{
		return if(this.orientation == ScrollBarOrientation.Vertical)
		{
			this._scrollView.scrollY
		}
		else
		{
			this._scrollView.scrollX
		}
	}
	
	private fun getSizeWithoutPaddings(): Int
	{
		return if(this.orientation == ScrollBarOrientation.Vertical)
		{
			this.height
		}
		else
		{
			this.width
		}
	}
	
	private fun getPaddings(): Int
	{
		return if(this.orientation == ScrollBarOrientation.Vertical)
		{
			this.paddingTop + this.paddingBottom
		}
		else
		{
			this.paddingStart + this.paddingEnd
		}
	}
	
	private fun getSize(): Int
	{
		return this.getSizeWithoutPaddings() - this.getPaddings()
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
		if(this.orientation == ScrollBarOrientation.Vertical)
		{
			this._scrollView.scrollY = position
		}
		else
		{
			this._scrollView.scrollX = position
		}
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		val position = if(this.orientation == ScrollBarOrientation.Vertical)
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
		this.backgroundColor?.apply {
			painter.color = this
		}
		
		canvas?.drawRect(
			0f,
			0f,
			this.width.toFloat(),
			this.height.toFloat(),
			this.painter
		)
		
		this.drawTrack(canvas)
		
		this.drawThumb(canvas)
		
		this.invalidate()
	}
	
	private fun drawTrack(canvas: Canvas?)
	{
		this.trackColor?.apply {
			painter.color = this
		}
		
		this.trackThickness?.apply {
			painter.strokeWidth = this
		}
		
		this.painter.strokeCap = Paint.Cap.ROUND
		
		if(this.orientation == ScrollBarOrientation.Horizontal)
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
		this.color?.apply {
			painter.color = this
		}
		
		val thickness = this.thumbThickness!!
		val roundness = this.thumbRoundness!!
		val position = this.getPosition()
		val size = this.getSizeFromContent()
		
		if(this.orientation == ScrollBarOrientation.Horizontal)
		{
			val centerVertical = (this.height / 2f) - (thickness / 2f)
			val left = position + this.paddingStart
			val right = position + size - this.paddingEnd
			canvas?.drawRoundRect(
				left,
				centerVertical,
				right,
				centerVertical + thickness ,
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