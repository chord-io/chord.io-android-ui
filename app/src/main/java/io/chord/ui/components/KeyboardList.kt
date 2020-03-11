package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import io.chord.R
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.KeyboardKeyBehavior
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.extensions.getChildOfType

class KeyboardList : LinearLayout, Zoomable
{
	private val zoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val keyBehavior = KeyboardKeyBehavior()
	private var initialized = false
	
	private var _orientation: ViewOrientation = ViewOrientation.Vertical
	private var _zoomDuration: Long = -1
	private var _whiteKeyColor: Int = -1
	private var _blackKeyColor: Int = -1
	private var _strokeColor: Int = -1
	private var _touchColor: Int = -1
	private var _strokeThickness: Float = 0f
	private var _octaves: Int = -1
	
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
			this.setOrientation(value.orientation)
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
	
	var strokeThickness: Float
		get() = this._strokeThickness
		set(value) {
			this._strokeThickness = value
			this.requestLayout()
			this.invalidate()
		}
	
	var octaves: Int
		get() = this._octaves
		set(value) {
			this._octaves = value
			this.generate()
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
			attrs, R.styleable.KeyboardList, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._orientation = typedArray.getInteger(
			R.styleable.KeyboardList_cio_kl_orientation,
			ViewOrientation.Vertical.orientation
		).let {
			ViewOrientation.values()[it]
		}
		
		this._zoomDuration = typedArray.getInteger(
			R.styleable.KeyboardList_cio_kl_zoomDuration,
			this.resources.getInteger(R.integer.keyboard_list_zoom_duration)
		).toLong()
		
		this._whiteKeyColor = typedArray.getColor(
			R.styleable.KeyboardList_cio_kl_whiteKeyColor,
			this.resources.getColor(R.color.textColorSecondary, theme)
		)
		
		this._blackKeyColor = typedArray.getColor(
			R.styleable.KeyboardList_cio_kl_blackKeyColor,
			this.resources.getColor(R.color.backgroundSecondary, theme)
		)
		
		this._strokeColor = typedArray.getColor(
			R.styleable.KeyboardList_cio_kl_strokeColor,
			this.resources.getColor(R.color.borderColorTernary, theme)
		)
		
		this._touchColor = typedArray.getColor(
			R.styleable.KeyboardList_cio_kl_touchColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this._strokeThickness = typedArray.getDimension(
			R.styleable.KeyboardList_cio_kl_strokeThickness,
			this.resources.getDimension(R.dimen.keyboard_list_stroke_thickness)
		)
		
		this._octaves = typedArray.getInteger(
			R.styleable.KeyboardList_cio_kl_octaves,
			this.resources.getInteger(R.integer.keyboard_list_octaves)
		)
		
		typedArray.recycle()
		
		this.setOrientation(this.orientation.orientation)
		
		this.zoomBehavior.widthAnimator.duration = this.zoomDuration
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		this.zoomBehavior.onEvaluateWidth = this::getKeyWidth
		this.zoomBehavior.onMeasureWidth = this::update
		this.zoomBehavior.onEvaluateHeight = this::getKeyWidth
		this.zoomBehavior.onMeasureHeight = this::update
		
		this.isClickable = true
		this.isFocusable = true
	}
	
	override fun onAttachedToWindow()
	{
		super.onAttachedToWindow()
		this.generate()
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
	
	override fun dispatchTouchEvent(event: MotionEvent): Boolean
	{
		val keyboards = this.getChildOfType<Keyboard>()
		keyboards.indices.forEach {index ->
			val keyboard = keyboards[index]
			keyboard.clearFocus()
			if(this.orientation == ViewOrientation.Horizontal)
			{
				val left = keyboard.left
				val right = keyboard.right
				if(left <= event.x && event.x <= right)
				{
					val x = if(index == 0)
					{
						event.x
					}
					else
					{
						event.x - left
					}
					val childEvent = MotionEvent.obtain(
						event.downTime,
						event.eventTime,
						event.action,
						x,
						event.y,
						event.metaState
					)
					keyboard.dispatchTouchEvent(childEvent)
				}
			}
			else
			{
				val top = keyboard.top
				val bottom = keyboard.bottom
				if(top <= event.y && event.y <= bottom)
				{
					val y = if(index == 0)
					{
						event.y
					}
					else
					{
						event.y - top
					}
					val childEvent = MotionEvent.obtain(
						event.downTime,
						event.eventTime,
						event.action,
						event.x,
						y,
						event.metaState
					)
					keyboard.dispatchTouchEvent(childEvent)
				}
			}
		}
		
		return super.dispatchTouchEvent(event)
	}
	
	override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean
	{
		return true
	}
	
	private fun generate()
	{
		this.removeAllViews()
		for(index in 0 until this.octaves)
		{
			val view = Keyboard(this.context, null)
			view.orientation = this.orientation
			view.zoomDuration = this.zoomDuration
			view.whiteKeyColor = this.whiteKeyColor
			view.blackKeyColor = this.blackKeyColor
			view.strokeColor = this.strokeColor
			view.touchColor = this.touchColor
			view.strokeThickness = this.strokeThickness
			
			if(index == 0)
			{
				view.clampOutsideLeftStroke = true
			}
			else if(index == this.octaves - 1)
			{
				view.clampOutsideRightStroke = true
			}
			else
			{
				view.clampOutsideStroke = true
			}
			
			if(this.orientation == ViewOrientation.Horizontal)
			{
				view.layoutParams = LayoutParams(this.layoutParams.width, LayoutParams.MATCH_PARENT)
			}
			else
			{
				view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, this.layoutParams.height)
			}
			
			this.addView(view)
		}
		this.update()
	}
	
	private fun update()
	{
		this.getChildOfType<Keyboard>().forEach {
			it.setZoomFactor(
				this.orientation,
				when(this.orientation)
				{
					ViewOrientation.Horizontal -> this.zoomBehavior.getFactorWidth()
					else -> this.zoomBehavior.getFactorHeight()
				},
				false
			)
		}
		this.requestLayout()
		this.invalidate()
	}
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
	{
		val stroke = this.strokeThickness
		val clampedStroke = -stroke

		if(this.orientation == ViewOrientation.Horizontal)
		{
			this.keyBehavior.measure(
				this.orientation,
				this.layoutParams,
				this.zoomBehavior.factorizedWidth,
				heightMeasureSpec,
				stroke
			)
			val measuredWidth = this.keyBehavior.white.width * 7f
			val measuredHeight = this.keyBehavior.white.height + stroke
			this.setMeasuredDimension(
				(measuredWidth * this.octaves + clampedStroke).toInt(),
				measuredHeight.toInt()
			)
			
			this.update()
			
			val childWidth = measuredWidth
			val childHeight = measuredHeight
			val childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth.toInt(), MeasureSpec.EXACTLY)
			val childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight.toInt(), MeasureSpec.EXACTLY)
			this.measureChildren(childWidthSpec, childHeightSpec)
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
			val measuredWidth = this.keyBehavior.white.width + stroke
			val measuredHeight = this.keyBehavior.white.height * 7f
			this.setMeasuredDimension(
				measuredWidth.toInt(),
				(measuredHeight * this.octaves + clampedStroke).toInt()
			)
			
			this.update()

			val childWidth = measuredWidth
			val childHeight = measuredHeight + clampedStroke
			val childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth.toInt(), MeasureSpec.EXACTLY)
			val childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight.toInt(), MeasureSpec.EXACTLY)
			this.measureChildren(childWidthSpec, childHeightSpec)
		}
	}
}