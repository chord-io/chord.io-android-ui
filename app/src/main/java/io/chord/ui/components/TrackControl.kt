package io.chord.ui.components

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.chord.R
import io.chord.ui.animations.HsvColorEvaluator
import io.chord.ui.extensions.*
import io.chord.ui.utils.RippleDrawableUtils
import java.util.*


class TrackControl : View, Binder
{
	private class StateContext(
		private val trackControl: TrackControl
	)
	{
		private interface State
		{
			fun changeColor(context: StateContext)
		}
		
		private class NormalState : State
		{
			override fun changeColor(context: StateContext)
			{
				context.backgroundMuteAnimator.setFloatValues(
					context.backgroundMuteOpacity,
					0f
				)
				context.backgroundSoloAnimator.setFloatValues(
					context.backgroundSoloOpacity,
					0f
				)
				context.textMuteAnimator.setIntValues(
					context.textMuteColor,
					context.trackControl.unselectedTextColor
				)
				context.textSoloAnimator.setIntValues(
					context.textSoloColor,
					context.trackControl.unselectedTextColor
				)
				context.ripple.setColor(RippleDrawableUtils.getColorStateList(
					context.trackControl.backgroundNormalColor,
					context.trackControl.backgroundNormalColor
				))
				
				context.animatorSet.start()
			}
		}
		
		private class MuteState : State
		{
			override fun changeColor(context: StateContext)
			{
				context.backgroundMuteAnimator.setFloatValues(
					context.backgroundMuteOpacity,
					1f
				)
				context.backgroundSoloAnimator.setFloatValues(
					context.backgroundSoloOpacity,
					0f
				)
				context.textMuteAnimator.setIntValues(
					context.textMuteColor,
					context.trackControl.selectedTextColor
				)
				context.textSoloAnimator.setIntValues(
					context.textSoloColor,
					context.trackControl.unselectedTextColor
				)
				context.ripple.setColor(RippleDrawableUtils.getColorStateList(
					context.trackControl.backgroundNormalColor,
					context.trackControl.backgroundMuteColor
				))
				
				context.animatorSet.start()
			}
		}
		
		private class SoloState : State
		{
			override fun changeColor(context: StateContext)
			{
				context.backgroundMuteAnimator.setFloatValues(
					context.backgroundMuteOpacity,
					0f
				)
				context.backgroundSoloAnimator.setFloatValues(
					context.backgroundSoloOpacity,
					1f
				)
				context.textMuteAnimator.setIntValues(
					context.textMuteColor,
					context.trackControl.unselectedTextColor
				)
				context.textSoloAnimator.setIntValues(
					context.textSoloColor,
					context.trackControl.selectedTextColor
				)
				context.ripple.setColor(RippleDrawableUtils.getColorStateList(
					context.trackControl.backgroundNormalColor,
					context.trackControl.backgroundSoloColor
				))
				
				context.animatorSet.start()
			}
		}
		
		private var state: State = NormalState()
		private var requestInvalidateCounter: Int = 0
		private val requestInvalidateThreshold: Int = 4
		
		private val backgroundMuteAnimator: ValueAnimator = ValueAnimator()
		private val textMuteAnimator: ValueAnimator = ValueAnimator()
		private val backgroundSoloAnimator: ValueAnimator = ValueAnimator()
		private val textSoloAnimator: ValueAnimator = ValueAnimator()
		val animatorSet: AnimatorSet = AnimatorSet()
		var backgroundMuteOpacity: Float = 0f
		var textMuteColor: Int = 0
		var backgroundSoloOpacity: Float = 0f
		var textSoloColor: Int = 0
		val ripple: RippleDrawable
		
		init
		{
			this.trackControl._backgroundMuteColor = this.trackControl
				.backgroundMuteColor
				.toTransparent(
					this.backgroundMuteOpacity
				)
			this.trackControl._backgroundSoloColor = this.trackControl
				.backgroundSoloColor
				.toTransparent(
					this.backgroundSoloOpacity
				)
			this.textMuteColor = this.trackControl.unselectedTextColor
			this.textSoloColor = this.trackControl.unselectedTextColor
			
			this.initBackgroundMuteAnimator()
			this.initBackgroundSoloAnimator()
			this.initTextMuteAnimator()
			this.initTextSoloAnimator()
			
			this.animatorSet.playTogether(
				this.backgroundMuteAnimator,
				this.backgroundSoloAnimator,
				this.textMuteAnimator,
				this.textSoloAnimator
			)
			this.animatorSet.duration = this.trackControl.animationDuration
			// TODO : set interpolator on another place
			this.animatorSet.interpolator = FastOutSlowInInterpolator()
			
			val radius = FloatArray(8)
			Arrays.fill(radius, this.trackControl.roundness)
			val shape = RoundRectShape(radius, null, null)
			val mask = ShapeDrawable(shape)
			
			this.ripple = RippleDrawableUtils.create(
				this.trackControl.backgroundNormalColor,
				this.trackControl.backgroundNormalColor,
				mask
			)
			
			this.trackControl.background = this.ripple
		}
		
		private fun initBackgroundMuteAnimator()
		{
			this.backgroundMuteAnimator.addUpdateListener {
				this.backgroundMuteOpacity = it.animatedValue as Float
				this.trackControl._backgroundMuteColor = this.trackControl
					.backgroundMuteColor
					.toTransparent(
						this.backgroundMuteOpacity
					)
				this.requestInvalidate()
			}
		}
		
		private fun initBackgroundSoloAnimator()
		{
			this.backgroundSoloAnimator.addUpdateListener {
				this.backgroundSoloOpacity = it.animatedValue as Float
				this.trackControl._backgroundSoloColor = this.trackControl
					.backgroundSoloColor
					.toTransparent(
						this.backgroundSoloOpacity
					)
				this.requestInvalidate()
			}
		}
		
		private fun initTextMuteAnimator()
		{
			this.textMuteAnimator.addUpdateListener {
				this.textMuteColor = it.animatedValue as Int
				this.requestInvalidate()
			}
			this.textMuteAnimator.setIntValues(
				this.trackControl.unselectedTextColor,
				this.trackControl.unselectedTextColor
			)
			this.textMuteAnimator.setEvaluator(HsvColorEvaluator())
		}
		
		private fun initTextSoloAnimator()
		{
			this.textSoloAnimator.addUpdateListener {
				this.textSoloColor = it.animatedValue as Int
				this.requestInvalidate()
			}
			this.textSoloAnimator.setIntValues(
				this.trackControl.unselectedTextColor,
				this.trackControl.unselectedTextColor
			)
			this.textSoloAnimator.setEvaluator(HsvColorEvaluator())
		}
		
		fun setState(state: TrackControlState)
		{
			this.state = when(state)
			{
				TrackControlState.Normal -> NormalState()
				TrackControlState.Mute -> MuteState()
				TrackControlState.Solo -> SoloState()
			}
		}
		
		fun invalidate()
		{
			this.state.changeColor(this)
		}
		
		private fun requestInvalidate()
		{
			this.requestInvalidateCounter += 1
			
			if(this.requestInvalidateCounter < this.requestInvalidateThreshold)
			{
				return
			}
			
			this.trackControl.invalidate()
			this.requestInvalidateCounter = 0
		}
	}
	
	private val controllables: MutableMap<Int, TrackControl> = mutableMapOf()
	private lateinit var stateContext: StateContext
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

	private var _state: TrackControlState = TrackControlState.Normal
	private var _animationDuration: Long = -1
	private var _backgroundNormalColor: Int = -1
	private var _backgroundMuteColor: Int = -1
	private var _backgroundSoloColor: Int = -1
	private var _unselectedTextColor: Int = -1
	private var _selectedTextColor: Int = -1
	private var _roundness: Float = -1f
	private var _textSize: Float = -1f
	private var _textMute: String = ""
	private var _textSolo: String = ""
	
	var state: TrackControlState
		get() = this._state
		set(value) {
			this._state = value
			this.controllables.forEach { (_, trackControl) ->
				trackControl.state = value
			}
			this.stateContext.setState(value)
			this.stateContext.invalidate()
		}
	
	var animationDuration: Long
		get() = this._animationDuration
		set(value) {
			this._animationDuration = value
			this.stateContext.animatorSet.duration = value
		}
	
	var backgroundNormalColor: Int
		get() = this._backgroundNormalColor
		set(value) {
			this._backgroundNormalColor = value
			this.stateContext.ripple.setBackgroundColor(value)
			this.stateContext.invalidate()
		}
	
	var backgroundMuteColor: Int
		get() = this._backgroundMuteColor
		set(value) {
			this._backgroundMuteColor = value
			this.stateContext.invalidate()
		}
	
	var backgroundSoloColor: Int
		get() = this._backgroundSoloColor
		set(value) {
			this._backgroundSoloColor = value
			this.stateContext.invalidate()
		}
	
	var unselectedTextColor: Int
		get() = this._unselectedTextColor
		set(value) {
			this._unselectedTextColor = value
			this.stateContext.invalidate()
		}
	
	var selectedTextColor: Int
		get() = this._selectedTextColor
		set(value) {
			this._selectedTextColor = value
			this.stateContext.invalidate()
		}
	
	var roundness: Float
		get() = this._roundness
		set(value) {
			this._roundness = value
			// TODO set ripple roundness
		}
	
	var textSize: Float
		get() = this._textSize
		set(value) {
			this._textSize = value
			this.invalidate()
		}
	
	var textMute: String
		get() = this._textMute
		set(value) {
			this._textMute = value
			this.invalidate()
		}
	
	var textSolo: String
		get() = this._textSolo
		set(value) {
			this._textSolo = value
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
	
	private fun init(attrs: AttributeSet?, defStyle: Int) {
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.TrackControl, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._state = typedArray.getInteger(
			R.styleable.TrackControl_cio_tc_state,
			TrackControlState.Normal.state
		).let {
			TrackControlState.values()[it]
		}
		
		this._animationDuration = typedArray.getInteger(
			R.styleable.TrackControl_cio_tc_animationDuration,
			this.resources.getInteger(R.integer.track_control_animation_duration)
		).toLong()
		
		this._backgroundNormalColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_backgroundNormalColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._backgroundMuteColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_backgroundMuteColor,
			this.resources.getColor(R.color.warningColor, theme)
		)
		
		this._backgroundSoloColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_backgroundSoloColor,
			this.resources.getColor(R.color.errorColor, theme)
		)
		
		this._unselectedTextColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_unselectedTextColor,
			this.resources.getColor(R.color.textColorSecondary, theme)
		)
		
		this._selectedTextColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_selectedTextColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._roundness = typedArray.getDimension(
			R.styleable.TrackControl_cio_tc_roundness,
			this.resources.getDimension(R.dimen.track_control_roundness)
		)
		
		this._textSize = typedArray.getDimension(
			R.styleable.TrackControl_cio_tc_textSize,
			this.resources.getDimension(R.dimen.track_control_text_size)
		)
		
		this._textMute = typedArray.getString(
			R.styleable.TrackControl_cio_tc_textMute
		)!!
		
		this._textSolo = typedArray.getString(
			R.styleable.TrackControl_cio_tc_textSolo
		)!!

		typedArray.recycle()
		
		this.stateContext = StateContext(this)
		
		if(this.id == -1)
		{
			this.id = generateViewId()
		}
		
		this.isClickable = true
		this.isFocusable = false
		this.isFocusableInTouchMode = false
	}
	
	override fun attach(id: Int)
	{
		val rootView = this.getParentRootView()
		val controllable = rootView.findViewById<TrackControl>(id)
		controllable.controllables.clear()
		controllable.state = this.state
		this.controllables[id] = controllable
	}
	
	override fun detach(id: Int)
	{
		this.controllables.remove(id)
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		if(event.action == MotionEvent.ACTION_DOWN)
		{
			val position = event.x
			val halfWidth = this.width / 2f
			val halfHeight = this.height / 2f

			this.state = when
			{
				position <= halfWidth && this.state != TrackControlState.Mute -> TrackControlState.Mute
				position > halfWidth && this.state != TrackControlState.Solo -> TrackControlState.Solo
				else -> TrackControlState.Normal
			}
			
			this.stateContext.ripple.setHotspot(halfWidth, halfHeight)
		}
		
		super.onTouchEvent(event)
		return true
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		this.drawMuteButton(canvas!!)
		this.drawSoloButton(canvas)
	}
	
	private fun drawMuteButton(canvas: Canvas)
	{
		this.painter.color = this.backgroundMuteColor
		
		val bounds = canvas.clipBounds.toRectF()
		bounds.set(
			bounds.left,
			bounds.top,
			bounds.right - bounds.width() / 2f,
			bounds.bottom
		)
		
		canvas.drawPath(
			bounds.round(
				this.roundness,
				0f,
				0f,
				this.roundness
			),
			this.painter
		)
		
		this.painter.color = this.stateContext.textMuteColor
		this.painter.textSize = this.textSize
		
		val textPosition = this.textMute.getTextCentered(
			bounds.centerX().toInt(),
			bounds.centerY().toInt(),
			this.painter
		)
		
		canvas.drawText(
			this.textMute,
			textPosition.x,
			textPosition.y,
			this.painter
		)
	}
	
	private fun drawSoloButton(canvas: Canvas)
	{
		this.painter.color = this.backgroundSoloColor
		
		val bounds = canvas.clipBounds.toRectF()
		bounds.set(
			bounds.left + bounds.width() / 2f,
			bounds.top,
			bounds.right,
			bounds.bottom
		)
		
		canvas.drawPath(
			bounds.round(
				0f,
				this.roundness,
				this.roundness,
				0f
			),
			this.painter
		)
		
		this.painter.color = this.stateContext.textSoloColor
		this.painter.textSize = this.textSize
		
		val textPosition = this.textSolo.getTextCentered(
			bounds.centerX().toInt(),
			bounds.centerY().toInt(),
			this.painter
		)
		
		canvas.drawText(
			this.textSolo,
			textPosition.x,
			textPosition.y,
			this.painter
		)
	}
}