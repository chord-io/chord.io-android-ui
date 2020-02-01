package io.chord.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.chord.R
import io.chord.ui.extensions.getParentRootView
import io.chord.ui.gestures.GestureDetector
import io.chord.ui.gestures.SimpleOnGestureListener
import io.chord.ui.utils.MathUtils

class ScrollBar : View, Binder
{
	private class Thumb(
		private val scrollBar: ScrollBar
	)
	{
		val position: Float
		val size: Float
		
		init
		{
			val scrollViewContentSize = this.scrollBar.getScrollViewContentSize().toFloat()
			val scrollViewSize = this.scrollBar.getScrollViewSize().toFloat()
			val scrollBarSize = this.scrollBar.getSize().toFloat()
			val scrollViewPosition = this.scrollBar.getScrollViewPosition().toFloat()
			
			if(scrollViewContentSize <= scrollViewSize)
			{
				this.size = scrollBarSize
			}
			else
			{
				val ratio = MathUtils.ratio(scrollViewSize, scrollViewContentSize)
				val size = ratio * scrollBarSize
				
				if(size < this.scrollBar.thumbThickness)
				{
					this.size = this.scrollBar.thumbThickness
				}
				else
				{
					this.size = size
				}
			}
			
			this.position = MathUtils.map(
				scrollViewPosition,
				0f,
				scrollViewContentSize - scrollViewSize,
				0f,
				scrollBarSize - this.size
			)
		}
	}
	
	private class Controller(
		id: Int,
		private val scrollBar: ScrollBar
	)
	{
		private val scrollView: FrameLayout
		private var size: Int = 0
		private var contentSize: Int = 0
		
		init
		{
			val rootView = this.scrollBar.getParentRootView()
			this.scrollView = when(val scrollView = rootView.findViewById<View>(id))
			{
				is TwoDimensionalScrollView ->
				{
					if(scrollBar.orientation == ViewOrientation.Vertical)
					{
						scrollView.verticalScrollView
					} else
					{
						scrollView.horizontalScrollView
					}
				}
				is ScrollView -> scrollView
				is HorizontalScrollView -> scrollView
				else -> throw IllegalStateException("view is not a scrollview")
			}
			
			this.scrollView.isVerticalScrollBarEnabled = false
			this.scrollView.isHorizontalScrollBarEnabled = false
			
			this.scrollView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
				val oldContentSize = this.contentSize
				val oldSize = this.size
				val contentSize = this.getContentSize()
				val size = this.getSize()
				val position = this.getPosition()
				
				when
				{
					oldSize == 0 || oldContentSize == 0 -> return@addOnLayoutChangeListener
					else -> this.scrollBar.onResizeEvent(
						size,
						contentSize,
						oldSize,
						oldContentSize,
						position
					)
				}
			}
			
			this.scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
				this.scrollBar.invalidate()
			}
		}
		
		override fun equals(other: Any?): Boolean
		{
			if(other is Controller)
			{
				when
				{
					this.getSizeWithoutPadding() != other.getSizeWithoutPadding() ->
					{
						return false
					}
					this.getContentSize() != other.getContentSize() ->
					{
						return false
					}
					this.getPaddingLeft() != other.getPaddingLeft() ->
					{
						return false
					}
					this.getPaddingRight() != other.getPaddingRight() ->
					{
						return false
					}
					else -> return true
				}
			}
			else
			{
				return false
			}
		}
		
		override fun hashCode(): Int
		{
			return (
					this.getSizeWithoutPadding() +
							this.getContentSize() +
							this.getPaddingLeft() +
							this.getPaddingRight()
					).hashCode()
		}
		
		fun getContentSize(): Int
		{
			if(this.scrollView.childCount > 0)
			{
				val child = scrollView.getChildAt(0)
				
				this.contentSize = if(this.scrollBar.orientation == ViewOrientation.Vertical)
				{
					child.height
				}
				else
				{
					child.width
				}
				
				return this.contentSize
			}
			
			this.contentSize = this.getSizeWithoutPadding()
			return this.contentSize
		}
		
		fun getSizeWithoutPadding(): Int
		{
			return if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				this.scrollView.height
			}
			else
			{
				this.scrollView.width
			}
		}
		
		fun getSize(): Int
		{
			this.size = this.getSizeWithoutPadding() - this.getPaddingLeft() - this.getPaddingRight()
			return this.size
		}
		
		fun getPaddingLeft(): Int
		{
			return if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				this.scrollView.paddingTop
			}
			else
			{
				this.scrollView.paddingStart
			}
		}
		
		fun getPaddingRight(): Int
		{
			return if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				this.scrollView.paddingBottom
			}
			else
			{
				this.scrollView.paddingEnd
			}
		}
		
		fun getPosition(): Int
		{
			return if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				this.scrollView.scrollY
			}
			else
			{
				this.scrollView.scrollX
			}
		}
		
		fun setPosition(position: Int)
		{
			return if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				this.scrollView.scrollY = position
			}
			else
			{
				this.scrollView.scrollX = position
			}
		}
	}
	
	private class GestureListener(
		private val scrollBar: ScrollBar
	) : SimpleOnGestureListener()
	{
		private var positionSource: Int = 0
		private var positionDestination: Int = 0
		
		init
		{
			this.scrollBar.positionAnimator.addUpdateListener {
				val position = it.animatedValue as Int
				this.scrollBar.setPosition(position)
				this.scrollBar.invalidate()
			}
			
			// TODO : set interpolator on another place
			this.scrollBar.positionAnimator.interpolator = FastOutSlowInInterpolator()
		}
		
		override fun onDown(event: MotionEvent): Boolean {
			this.scrollBar.requestFocus()
			return true
		}
		
		override fun onUp(event: MotionEvent): Boolean
		{
			this.scrollBar.clearFocus()
			return true
		}
		
		override fun onScroll(
			event1: MotionEvent,
			event2: MotionEvent,
			distanceX: Float,
			distanceY: Float
		): Boolean {
			this.setPosition(event2)
			this.scrollBar.setPosition(this.scrollBar.position)
			return true
		}
		
		override fun onDoubleTap(event: MotionEvent): Boolean {
			val limit = this.scrollBar.getLimitPosition()
			val position = this.scrollBar.position
			val ratio = MathUtils.ratio(position, limit)
			val invertedPosition = ((1 - ratio) * limit).toInt()
			
			this.beginConfigureAnimation()
			this.scrollBar.position = invertedPosition
			this.endConfigureAnimation()
			return true
		}
		
		override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
			this.beginConfigureAnimation()
			this.setPosition(event)
			this.endConfigureAnimation()
			return true
		}
		
		override fun onLongPress(event: MotionEvent)
		{
			val limit = this.scrollBar.getLimitPosition()
			val halfLimit = limit / 2f
			
			this.beginConfigureAnimation()
			
			if(this.scrollBar.position <= halfLimit)
			{
				this.scrollBar.position = limit
			}
			else
			{
				this.scrollBar.position = 0
			}
			
			this.endConfigureAnimation()
		}
		
		private fun beginConfigureAnimation()
		{
			this.positionSource = this.scrollBar.position
		}
		
		private fun endConfigureAnimation()
		{
			this.positionDestination = this.scrollBar.position
			this.scrollBar.positionAnimator.setIntValues(
				this.positionSource,
				this.positionDestination
			)
			this.scrollBar.positionAnimator.start()
		}
		
		private fun setPosition(event: MotionEvent)
		{
			val position = if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				event.y
			}
			else
			{
				event.x
			}
			
			val scrollBarSize = this.scrollBar.getSizeWithoutPadding()
			val scrollContentSize = this.scrollBar.getScrollViewContentSize()
			val scrollViewSize = this.scrollBar.getScrollViewSize()
			val scaledPosition = MathUtils.map(
				position.toInt(),
				0,
				scrollBarSize,
				0,
				scrollContentSize - scrollViewSize
			)
			
			this.scrollBar.position = scaledPosition
		}
	}
	
	private val controllers: MutableMap<Int, Controller> = mutableMapOf()
	private val positionAnimator: ValueAnimator = ValueAnimator()
	private val gestureDetector: GestureDetector = GestureDetector(
		this.context,
		GestureListener(this)
	)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	private var position: Int = 0
	
	private var _orientation: ViewOrientation = ViewOrientation.Horizontal
	private var _moveDuration: Long = -1
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
	
	var moveDuration: Long
		get() = this._moveDuration
		set(value) {
			this._moveDuration = value
			this.positionAnimator.duration = value
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
		
		this.moveDuration = typedArray.getInteger(
			R.styleable.ScrollBar_cio_sb_moveDuration,
			this.resources.getInteger(R.integer.scrollbar_move_duration)
		).toLong()
		
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
	
	override fun attach(id: Int)
	{
		this.controllers[id] = Controller(id, this)
	}
	
	override fun detach(id: Int)
	{
		this.controllers.remove(id)
	}
	
	private fun checkScrollBarControllerAreEquals()
	{
		if(this.controllers.isEmpty() || this.controllers.size == 1)
		{
			return
		}
		
		val result = this.controllers
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
		return this.controllers.values.first().getContentSize()
	}
	
	private fun getScrollViewSize(): Int
	{
		this.checkScrollBarControllerAreEquals()
		return this.controllers.values.first().getSize()
	}
	
	private fun getScrollViewPosition(): Int
	{
		this.checkScrollBarControllerAreEquals()
		return this.controllers.values.first().getPosition()
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
	
	private fun getLimitPosition(): Int
	{
		val contentSize = this.getScrollViewContentSize()
		val scrollViewSize = this.getScrollViewSize()
		
		return if(contentSize <= scrollViewSize)
		{
			scrollViewSize
		} else
		{
			contentSize - scrollViewSize
		}
	}
	
	private fun setPosition(position: Int)
	{
		this.checkScrollBarControllerAreEquals()
		this.controllers.forEach {
			it.value.setPosition(position)
		}
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
	
	private fun onResizeEvent(
		scrollViewSize: Int,
		scrollViewContentSize: Int,
		oldScrollViewSize: Int,
		oldScrollViewContentSize: Int,
		position: Int)
	{
		// TODO see what happen when multiple scroll view are controlled
		
		val scaledPosition = MathUtils.map(
			position,
			0,
			oldScrollViewContentSize - oldScrollViewSize,
			0,
			scrollViewContentSize - scrollViewSize
		)
		
		this.position = scaledPosition
		this.setPosition(scaledPosition)
		
		this.invalidate()
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		this.drawTrack(canvas!!)
		
		if(this.controllers.isNotEmpty())
		{
			this.drawThumb(canvas)
		}
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
	
	private fun drawThumb(canvas: Canvas)
	{
		this.painter.color = this.thumbColor
		
		val thickness = this.thumbThickness
		val roundness = this.thumbRoundness
		val thumb = Thumb(this)
		
		if(this.orientation == ViewOrientation.Horizontal)
		{
			val centerVertical = (this.height / 2f) - (thickness / 2f)
			val left = thumb.position + this.paddingStart
			val right = left + thumb.size
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
			val top = thumb.position + this.paddingTop
			val bottom = top + thumb.size
			canvas.drawRoundRect(
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