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
import androidx.fragment.app.Fragment
import io.chord.R
import io.chord.ui.animations.FastOutSlowInValueAnimator
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.Binder
import io.chord.ui.behaviors.OrientedBoundBehavior
import io.chord.ui.behaviors.PropertyBehavior
import io.chord.ui.extensions.getParentRootView
import io.chord.ui.gestures.GestureDetector
import io.chord.ui.gestures.SimpleOnGestureListener
import io.chord.ui.utils.MathUtils

class ScrollBar : View, Binder
{
	private class ScrollBarBoundBehavior(
		private val scrollBar: ScrollBar
	): OrientedBoundBehavior(scrollBar)
	{
		fun checkScrollBarControllerAreEquals()
		{
			if(this.scrollBar.bindBehavior.controls.isEmpty() || this.scrollBar.bindBehavior.controls.size == 1)
			{
				return
			}
			
			val result = this.scrollBar.bindBehavior.controls
				.stream()
				.distinct()
			
			if(result.count() > 1)
			{
				throw java.lang.IllegalStateException("all scrollviews must have the same dimension, including content and padding")
			}
		}
		
		fun getScrollViewContentSize(): Int
		{
			this.checkScrollBarControllerAreEquals()
			return this.scrollBar.bindBehavior.controls.first().boundBehavior.getContentSize()
		}
		
		fun getScrollViewSize(): Int
		{
			this.checkScrollBarControllerAreEquals()
			return this.scrollBar.bindBehavior.controls.first().boundBehavior.getSize()
		}
		
		fun getScrollViewPosition(): Int
		{
			this.checkScrollBarControllerAreEquals()
			return this.scrollBar.bindBehavior.controls.first().boundBehavior.getPosition()
		}
		
		fun getLimitPosition(): Int
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
	}
	
	private class ControllerBoundBehavior(
		private val scrollView: FrameLayout
	) : OrientedBoundBehavior(scrollView)
	{
		private var _size: Int = 0
		private var _contentSize: Int = 0
		
		val lastMeasure: Int
			get() = this._size
		
		val lastContentMeasure: Int
			get() = this._contentSize
		
		override fun getSize(): Int
		{
			this._size = super.getSize()
			return this._size
		}
		
		fun getContentSize(): Int
		{
			if(this.scrollView.childCount > 0)
			{
				val child = scrollView.getChildAt(0)
				
				this._contentSize = if(this.orientation == ViewOrientation.Vertical)
				{
					child.height
				}
				else
				{
					child.width
				}
				
				return this._contentSize
			}
			
			this._contentSize = this.getSizeWithoutPadding()
			return this._contentSize
		}
		
		fun getPosition(): Int
		{
			return if(this.orientation == ViewOrientation.Vertical)
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
			return if(this.orientation == ViewOrientation.Vertical)
			{
				this.scrollView.scrollY = position
			}
			else
			{
				this.scrollView.scrollX = position
			}
		}
	}
	
	private class Thumb(
		private val scrollBar: ScrollBar
	)
	{
		val position: Float
		val size: Float
		
		init
		{
			val scrollViewContentSize = this.scrollBar.boundBehavior.getScrollViewContentSize().toFloat()
			val scrollViewSize = this.scrollBar.boundBehavior.getScrollViewSize().toFloat()
			val scrollBarSize = this.scrollBar.boundBehavior.getSize().toFloat()
			val scrollViewPosition = this.scrollBar.boundBehavior.getScrollViewPosition().toFloat()
			
			if(scrollViewContentSize <= scrollViewSize)
			{
				this.size = scrollBarSize
				this.position = 0f
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
				
				this.position = MathUtils.map(
					scrollViewPosition,
					0f,
					scrollViewContentSize - scrollViewSize,
					0f,
					scrollBarSize - this.size
				)
			}
		}
	}
	
	private class Control : Bindable
	{
		private val scrollBar: ScrollBar
		private val scrollView: FrameLayout
		private val bindableBehavior = BindableBehavior(this)
		val boundBehavior: ControllerBoundBehavior
		
		constructor(id: Int, scrollBar: ScrollBar)
		{
			this.scrollBar = scrollBar
			val rootView = this.scrollBar.getParentRootView()
			val scrollView = rootView.findViewById<View>(id)
			this.scrollView = this.initScrollView(scrollView)
			this.boundBehavior = ControllerBoundBehavior(this.scrollView)
			this.boundBehavior.orientation = this.scrollBar.orientation
		}
		
		constructor(view: View, scrollBar: ScrollBar)
		{
			this.scrollBar = scrollBar
			this.scrollView = this.initScrollView(view)
			this.boundBehavior = ControllerBoundBehavior(this.scrollView)
			this.boundBehavior.orientation = this.scrollBar.orientation
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
		
		private fun initScrollView(view: View): FrameLayout
		{
			val scrollView = when(view)
			{
				is TwoDimensionalScrollView ->
				{
					if(scrollBar.orientation == ViewOrientation.Vertical)
					{
						view.verticalScrollView
					} else
					{
						view.horizontalScrollView
					}
				}
				is ScrollView -> view
				is HorizontalScrollView -> view
				else -> throw IllegalStateException("view is not a scrollview")
			}
			
			scrollView.isVerticalScrollBarEnabled = false
			scrollView.isHorizontalScrollBarEnabled = false
			
			scrollView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
				val oldContentSize = this.boundBehavior.lastContentMeasure
				val oldSize = this.boundBehavior.lastMeasure
				val contentSize = this.boundBehavior.getContentSize()
				val size = this.boundBehavior.getSize()
				val position = this.boundBehavior.getPosition()
				
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
			
			scrollView.setOnScrollChangeListener { _, scrollX, scrollY, _, _ ->
				val position = if(this.scrollBar.orientation == ViewOrientation.Horizontal)
				{
					scrollX
				}
				else
				{
					scrollY
				}
				
				this.scrollBar.positionBehavior.setValue(position)
				this.scrollBar.invalidate()
			}
			
			return scrollView
		}
		
		override fun equals(other: Any?): Boolean
		{
			if(other is Control)
			{
				when
				{
					this.boundBehavior.getSizeWithoutPadding() != other.boundBehavior.getSizeWithoutPadding() ->
					{
						return false
					}
					this.boundBehavior.getContentSize() != other.boundBehavior.getContentSize() ->
					{
						return false
					}
					this.boundBehavior.getPaddingStart() != other.boundBehavior.getPaddingStart() ->
					{
						return false
					}
					this.boundBehavior.getPaddingEnd() != other.boundBehavior.getPaddingEnd() ->
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
					this.boundBehavior.getSizeWithoutPadding() +
							this.boundBehavior.getContentSize() +
							this.boundBehavior.getPaddingStart() +
							this.boundBehavior.getPaddingEnd()
					).hashCode()
		}
	}
	
	private class GestureListener(
		private val scrollBar: ScrollBar
	) : SimpleOnGestureListener()
	{
		private var positionSource: Int = 0
		private var positionDestination: Int = 0
		
		val positionAnimator: ValueAnimator = FastOutSlowInValueAnimator()
		
		init
		{
			// TODO extract scroll gesture to a behavior class
			
			this.positionAnimator.addUpdateListener {
				val position = it.animatedValue as Int
				this.scrollBar.positionBehavior.setValue(position)
				this.scrollBar.invalidate()
			}
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
			this.scrollBar.positionBehavior.setValue(this.scrollBar.positionBehavior.getValue())
			return true
		}
		
		override fun onDoubleTap(event: MotionEvent): Boolean {
			val limit = this.scrollBar.boundBehavior.getLimitPosition()
			val position = this.scrollBar.positionBehavior.getValue()
			val ratio = MathUtils.ratio(position, limit)
			val invertedPosition = ((1 - ratio) * limit).toInt()
			
			this.beginConfigureAnimation()
			this.scrollBar.positionBehavior.setValue(invertedPosition)
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
			val limit = this.scrollBar.boundBehavior.getLimitPosition()
			val halfLimit = limit / 2f
			val position = this.scrollBar.positionBehavior.getValue()
			
			this.beginConfigureAnimation()
			
			if(position <= halfLimit)
			{
				this.scrollBar.positionBehavior.setValue(limit)
			}
			else
			{
				this.scrollBar.positionBehavior.setValue(0)
			}
			
			this.endConfigureAnimation()
		}
		
		private fun beginConfigureAnimation()
		{
			this.positionSource = this.scrollBar.positionBehavior.getValue()
		}
		
		private fun endConfigureAnimation()
		{
			this.positionDestination = this.scrollBar.positionBehavior.getValue()
			this.positionAnimator.setIntValues(
				this.positionSource,
				this.positionDestination
			)
			this.positionAnimator.start()
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
			
			val scrollBarSize = this.scrollBar.boundBehavior.getSizeWithoutPadding()
			val scrollContentSize = this.scrollBar.boundBehavior.getScrollViewContentSize()
			val scrollViewSize = this.scrollBar.boundBehavior.getScrollViewSize()
			val scaledPosition = MathUtils.map(
				position.toInt(),
				0,
				scrollBarSize,
				0,
				scrollContentSize - scrollViewSize
			)
			
			this.scrollBar.positionBehavior.setValue(scaledPosition)
		}
	}
	
	private val bindBehavior: BindBehavior<Control> = BindBehavior(this)
	private val boundBehavior = ScrollBarBoundBehavior(this)
	private val positionBehavior = PropertyBehavior(0)
	private val gestureListener: GestureListener = GestureListener(this)
	private val gestureDetector: GestureDetector = GestureDetector(
		this.context,
		this.gestureListener
	)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
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
			this.boundBehavior.orientation = this._orientation
			this.bindBehavior.controls.forEach {
				it.boundBehavior.orientation = this._orientation
			}
			this.invalidate()
		}
	
	var moveDuration: Long
		get() = this._moveDuration
		set(value) {
			this._moveDuration = value
			this.gestureListener.positionAnimator.duration = value
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
		
		this._orientation = typedArray.getInteger(
			R.styleable.ScrollBar_cio_sb_orientation,
			ViewOrientation.Horizontal.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		this._moveDuration = typedArray.getInteger(
			R.styleable.ScrollBar_cio_sb_moveDuration,
			this.resources.getInteger(R.integer.scrollbar_move_duration)
		).toLong()
		
		this._trackColor = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_trackColor,
			this.resources.getColor(R.color.borderColorPrimary, theme)
		)
		
		this._thumbColor = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_thumbColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this._trackThickness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_trackThickness,
			this.resources.getDimension(R.dimen.scrollbar_track_thickness)
		)
		
		this._thumbThickness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_thumbThickness,
			this.resources.getDimension(R.dimen.scrollbar_thumb_thickness)
		)
		
		this._thumbRoundness = typedArray.getDimension(
			R.styleable.ScrollBar_cio_sb_thumbRoundness,
			this.resources.getDimension(R.dimen.scrollbar_thumb_roundness)
		)
		
		typedArray.recycle()
		
		this.gestureListener.positionAnimator.duration = this.moveDuration
		
		this.boundBehavior.orientation = this._orientation
		
		this.bindBehavior.onAttach = {
			this.boundBehavior.checkScrollBarControllerAreEquals()
		}
		this.bindBehavior.onDispatchEvent = {
			it.boundBehavior.setPosition(this.positionBehavior.getValue())
		}
		
		this.positionBehavior.onValueChanged = {
			this.bindBehavior.requestDispatchEvent()
		}
	}
	
	override fun attach(id: Int)
	{
		val control = Control(id, this)
		this.bindBehavior.attach(id, control)
	}
	
	override fun attach(view: View)
	{
		val control = Control(view, this)
		this.bindBehavior.attach(view.id, control)
	}
	
	override fun attach(fragment: Fragment)
	{
		// TODO Make control for fragment
		this.bindBehavior.attach(fragment)
	}
	
	override fun attachAll(views: List<View>)
	{
		views.forEach {
			val control = Control(it, this)
			this.bindBehavior.attach(it.id, control)
		}
	}
	
	override fun detach(id: Int)
	{
		this.bindBehavior.detach(id)
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
		
		this.positionBehavior.setValue(scaledPosition)
		
		this.invalidate()
	}
	
	override fun onDraw(canvas: Canvas)
	{
		this.drawTrack(canvas)
		
		if(this.bindBehavior.controls.isNotEmpty())
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