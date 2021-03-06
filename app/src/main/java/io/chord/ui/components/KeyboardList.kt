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

open class KeyboardList : LinearLayout, Zoomable
{
	protected val zoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val keyBehavior = KeyboardKeyBehavior()
	
	private var _orientation: ViewOrientation = ViewOrientation.Vertical
	private var _zoomDuration: Long = -1
	private var _maskColor: Int = -1
	private var _whiteKeyColor: Int = -1
	private var _blackKeyColor: Int = -1
	private var _strokeColor: Int = -1
	private var _touchColor: Int = -1
	private var _strokeThickness: Float = 0f
	private var _textWeight: Float = -1f
	private var _keyWeight: Float = -1f
	private var _textMargin: Float = -1f
	private var _octaves: Int = -1
	private var _octaveOffset: Int = -1
	private var _showOctaves: Boolean = true
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
	
	var maskColor: Int
		get() = this._maskColor
		set(value) {
			this._maskColor = value
			this.getChildOfType<Keyboard>().forEach {
				it.maskColor = value
			}
		}
	
	var whiteKeyColor: Int
		get() = this._whiteKeyColor
		set(value) {
			this._whiteKeyColor = value
			this.getChildOfType<Keyboard>().forEach {
				it.whiteKeyColor = value
			}
		}
	
	var blackKeyColor: Int
		get() = this._blackKeyColor
		set(value) {
			this._blackKeyColor = value
			this.getChildOfType<Keyboard>().forEach {
				it.blackKeyColor = value
			}
		}
	
	var strokeColor: Int
		get() = this._strokeColor
		set(value) {
			this._strokeColor = value
			this.getChildOfType<Keyboard>().forEach {
				it.strokeColor = value
			}
			this.setBackgroundColor(value)
		}
	
	var touchColor: Int
		get() = this._touchColor
		set(value) {
			this._touchColor = value
			this.getChildOfType<Keyboard>().forEach {
				it.touchColor = value
			}
		}
	
	var strokeThickness: Float
		get() = this._strokeThickness
		set(value) {
			this._strokeThickness = value
			this.getChildOfType<Keyboard>().forEach {
				it.strokeThickness = value
			}
		}
	
	var textWeight: Float
		get() = this._textWeight
		set(value) {
			this._textWeight = value
			this.getChildOfType<Keyboard>().forEach {
				it.textWeight = value
			}
		}
	
	var keyWeight: Float
		get() = this._keyWeight
		set(value) {
			this._keyWeight = value
			this.getChildOfType<Keyboard>().forEach {
				it.keyWeight = value
			}
		}
	
	var textMargin: Float
		get() = this._textMargin
		set(value) {
			this._textMargin = value
			this.getChildOfType<Keyboard>().forEach {
				it.textMargin = value
			}
		}
	
	var octaves: Int
		get() = this._octaves
		set(value) {
			this._octaves = value
			this.generate()
			this.requestLayout()
			this.invalidate()
		}
	
	var octaveOffset: Int
		get() = this._octaveOffset
		set(value) {
			this._octaveOffset = value
			this.generate()
			this.requestLayout()
			this.invalidate()
		}
	
	var showOctaves: Boolean
		get() = this._showOctaves
		set(value) {
			this._showOctaves = value
			this.getChildOfType<Keyboard>().forEach {
				it.showOctave = value
			}
		}
	
	var disableTouchEvent: Boolean
		get() = this._disableTouchEvent
		set(value) {
			this._disableTouchEvent = value
			this.getChildOfType<Keyboard>().forEach {
				it.disableTouchEvent = value
			}
		}
	
	var clampOutsideStroke: Boolean
		get() = this._clampOutsideStroke
		set(value) {
			this._clampOutsideStroke = value
			this.getChildOfType<Keyboard>().forEach {
				it.clampOutsideStroke = value
			}
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
		
		this._maskColor = typedArray.getColor(
			R.styleable.KeyboardList_cio_kl_maskColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
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
		
		this._textWeight = typedArray.getFloat(
			R.styleable.KeyboardList_cio_kl_textWeight,
			this.resources.getInteger(R.integer.keyboard_list_text_weight) / 100f
		)
		
		this._keyWeight = typedArray.getFloat(
			R.styleable.KeyboardList_cio_kl_keyWeight,
			this.resources.getInteger(R.integer.keyboard_list_key_weight) / 100f
		)
		
		this._textMargin = typedArray.getDimension(
			R.styleable.KeyboardList_cio_kl_textMargin,
			this.resources.getDimension(R.dimen.keyboard_list_text_margin)
		)
		
		this._octaves = typedArray.getInteger(
			R.styleable.KeyboardList_cio_kl_octaves,
			this.resources.getInteger(R.integer.keyboard_list_octaves)
		)
		
		this._octaveOffset = typedArray.getInteger(
			R.styleable.KeyboardList_cio_kl_octaveOffset,
			this.resources.getInteger(R.integer.keyboard_list_octave_offset)
		)
		
		this._showOctaves = typedArray.getBoolean(
			R.styleable.KeyboardList_cio_kl_showOctaves,
			this.resources.getBoolean(R.bool.keyboard_list_show_octaves)
		)
		
		this._disableTouchEvent = typedArray.getBoolean(
			R.styleable.KeyboardList_cio_kl_disableTouchEvent,
			this.resources.getBoolean(R.bool.keyboard_list_disable_touch_event)
		)
		
		this._clampOutsideStroke = typedArray.getBoolean(
			R.styleable.KeyboardList_cio_kl_clampOutsideStroke,
			this.resources.getBoolean(R.bool.keyboard_list_clamp_outside_stroke)
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
		
		this.setBackgroundColor(this.strokeColor)
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
	
	protected fun internalSetOrienation(orientation: ViewOrientation)
	{
		this._orientation = orientation
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
		if(this.disableTouchEvent)
		{
			return false
		}
		
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
		val left = 0 + this.octaveOffset
		val right = this.octaves + this.octaveOffset
		val range = if(this.orientation == ViewOrientation.Horizontal)
		{
			left until right
		}
		else
		{
			right - 1 downTo left
		}
		
		for(index in range)
		{
			val view = Keyboard(this.context, null)
			view.orientation = this.orientation
			view.zoomDuration = this.zoomDuration
			view.whiteKeyColor = this.whiteKeyColor
			view.blackKeyColor = this.blackKeyColor
			view.strokeColor = this.strokeColor
			view.touchColor = this.touchColor
			view.strokeThickness = this.strokeThickness
			view.textWeight = this.textWeight
			view.keyWeight = this.keyWeight
			view.textMargin = this.textMargin
			view.showOctave = this.showOctaves
			view.disableTouchEvent = this.disableTouchEvent
			view.clampOutsideStroke = this.clampOutsideStroke
			view.octave = index
			
			if(this.orientation == ViewOrientation.Horizontal)
			{
				val layoutParameter = LayoutParams(this.layoutParams.width, LayoutParams.MATCH_PARENT)
				
				if(index > left && !this.clampOutsideStroke)
				{
					layoutParameter.marginStart = -this.strokeThickness.toInt()
				}
				else if(index > left)
				{
					layoutParameter.marginStart = this.strokeThickness.toInt()
				}
				
				view.layoutParams = layoutParameter
			}
			else
			{
				val layoutParameter = LayoutParams(LayoutParams.MATCH_PARENT, this.layoutParams.height)
				
				if(index < right && !this.clampOutsideStroke)
				{
					layoutParameter.topMargin = -this.strokeThickness.toInt()
				}
				else if(index < right)
				{
					layoutParameter.topMargin = this.strokeThickness.toInt()
				}
				
				view.layoutParams = layoutParameter
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
		this.zoomBehavior.requestMeasure()

		val stroke = this.strokeThickness
		val clampedStroke =  if(this.clampOutsideStroke && this.octaves > 1)
		{
			stroke
		}
		else if(this.clampOutsideStroke && this.octaves == 1)
		{
			0f
		}
		else
		{
			stroke * (this.octaves + 1)
		}

		if(this.orientation == ViewOrientation.Horizontal)
		{
			this.keyBehavior.measure(
				this.orientation,
				this.layoutParams,
				this.zoomBehavior.factorizedWidth,
				heightMeasureSpec,
				stroke,
				true
			)
			val measuredWidth = (this.keyBehavior.white.width * 7f) + (stroke * 6f)
			val measuredHeight = this.keyBehavior.white.height
			this.setMeasuredDimension(
				(measuredWidth * this.octaves + (clampedStroke * (this.octaves - 1))).toInt(),
				measuredHeight.toInt()
			)

			this.update()

			val childWidth = measuredWidth
			val childHeight = if(this.clampOutsideStroke)
			{
				measuredHeight
			}
			else
			{
				measuredHeight - stroke * 2f
			}
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
				stroke,
				true
			)
			val measuredWidth = this.keyBehavior.white.width
			val measuredHeight = (this.keyBehavior.white.height * 7f) + (stroke * 6f)
			this.setMeasuredDimension(
				measuredWidth.toInt(),
				(measuredHeight * this.octaves + (clampedStroke * (this.octaves - 1))).toInt()
			)

			this.update()

			val childWidth = if(this.clampOutsideStroke)
			{
				measuredWidth
			}
			else
			{
				measuredWidth - stroke * 2f
			}
			val childHeight = measuredHeight
			val childWidthSpec = MeasureSpec.makeMeasureSpec(childWidth.toInt(), MeasureSpec.EXACTLY)
			val childHeightSpec = MeasureSpec.makeMeasureSpec(childHeight.toInt(), MeasureSpec.EXACTLY)
			this.measureChildren(childWidthSpec, childHeightSpec)
		}
	}
}