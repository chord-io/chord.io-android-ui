package io.chord.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.graphics.toRectF
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.chord.R
import io.chord.ui.extensions.getOptimalTextSize
import io.chord.ui.utils.QuantizeUtils

class Ruler : View, Zoomable, Quantifiable
{
	private var barCount: Int = 10
	private var textSizeOptimum: Float = -1f
	private var textPosition: Float = -1f
	private var textHalfPadding: Float = -1f
	private var zoomfactor: Float = 1f
	private var factorizedWidth: Float = -1f
	private var factorAnimator: ValueAnimator = ValueAnimator()
	private var quantization: QuantizeUtils.Quantization = QuantizeUtils.Quantization(
		QuantizeUtils.QuantizeValue.First,
		QuantizeUtils.QuantizeMode.Natural
	)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private var _zoomDuration: Long = -1
	private var _defaultWidth: Float = -1f
	private var _ticksColor: Int = -1
	private var _textColor: Int = -1
	private var _ticksThickness: Float = -1f
	private var _ticksWeight: Float = -1f
	private var _textSize: Float = -1f
	private var _textMargin: Float = -1f
	private var _textPadding: Float = -1f
	
	var zoomDuration: Long
		get() = this._zoomDuration
		set(value) {
			this._zoomDuration = value
			this.factorAnimator.duration = value
		}
	
	var defaultWidth: Float
		get() = this._defaultWidth
		set(value) {
			this._defaultWidth = value
			this.setZoomFactor(ViewOrientation.Horizontal, this.zoomfactor, true)
		}
	
	var ticksColor: Int
		get() = this._ticksColor
		set(value) {
			this._ticksColor = value
			this.invalidate()
		}
	
	var textColor: Int
		get() = this._textColor
		set(value) {
			this._textColor = value
			this.invalidate()
		}
	
	var ticksThickness: Float
		get() = this._ticksThickness
		set(value) {
			this._ticksThickness = value
			this.invalidate()
		}
	
	var ticksWeight: Float
		get() = this._ticksWeight
		set(value) {
			this._ticksWeight = when
			{
				value > 1f -> 1f
				value < 0f -> 0f
				else -> value
			}
			this.invalidate()
		}
	
	var textSize: Float
		get() = this._textSize
		set(value) {
			this._textSize = value
			this.invalidate()
		}
	
	var textMargin: Float
		get() = this._textMargin
		set(value) {
			this._textMargin = value
			this.invalidate()
		}
	
	var textPadding: Float
		get() = this._textPadding
		set(value) {
			this._textPadding = value
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
			attrs, R.styleable.Ruler, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this.zoomDuration = typedArray.getInteger(
			R.styleable.Ruler_cio_rl_zoomDuration,
			this.resources.getInteger(R.integer.ruler_zoom_duration)
		).toLong()
		
		this.defaultWidth = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_defaultWidth,
			this.resources.getDimension(R.dimen.ruler_default_width)
		)
		
		this.ticksColor = typedArray.getColor(
			R.styleable.Ruler_cio_rl_ticksColor,
			this.resources.getColor(R.color.textColor, theme)
		)
		
		this.textColor = typedArray.getColor(
			R.styleable.Ruler_cio_rl_textColor,
			this.resources.getColor(R.color.textColor, theme)
		)
		
		this.ticksThickness = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_ticksThickness,
			this.resources.getDimension(R.dimen.ruler_tick_thickness)
		)
		
		this.ticksWeight = typedArray.getFloat(
			R.styleable.Ruler_cio_rl_ticksWeight,
			this.resources.getInteger(R.integer.ruler_ticks_wieght) / 100f
		)
		
		this.textSize = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textSize,
			this.resources.getDimension(R.dimen.ruler_text_size)
		)
		
		this.textMargin = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textMargin,
			this.resources.getDimension(R.dimen.ruler_text_margin)
		)
		
		this.textPadding = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textPadding,
			this.resources.getDimension(R.dimen.ruler_text_padding)
		)
		
		typedArray.recycle()
		
		// TODO : set interpolator on another place
		this.factorAnimator.interpolator = FastOutSlowInInterpolator()
		this.factorAnimator.addUpdateListener { animator ->
			val factor = animator.animatedValue as Float
			this.internalSetZoomFactor(factor)
		}
		this.factorAnimator.doOnEnd {
			val animator = it as ValueAnimator
			val factor = animator.animatedValue as Float
			this.internalSetZoomFactor(factor)
		}
	}
	
	private fun internalSetZoomFactor(factor: Float)
	{
		this.zoomfactor = factor
		this.factorizedWidth = this.defaultWidth * this.zoomfactor
		this.requestLayout()
		this.invalidate()
	}
	
	override fun setZoomFactor(orientation: ViewOrientation, factor: Float, animate: Boolean)
	{
		if(orientation == ViewOrientation.Horizontal)
		{
			when
			{
				animate ->
				{
					this.factorAnimator.cancel()
					this.factorAnimator.setFloatValues(this.zoomfactor, factor)
					this.factorAnimator.start()
				}
				else -> this.internalSetZoomFactor(factor)
			}
		}
	}
	
	override fun setQuantization(quantization: QuantizeUtils.Quantization)
	{
		this.quantization = quantization
		this.invalidate()
	}
	
	override fun onMeasure(
		widthMeasureSpec: Int,
		heightMeasureSpec: Int
	)
	{
		val width = this.factorizedWidth * this.barCount
		val height = MeasureSpec.getSize(heightMeasureSpec)
		this.setMeasuredDimension(width.toInt(), height)
	}
	
	override fun onLayout(
		changed: Boolean,
		left: Int,
		top: Int,
		right: Int,
		bottom: Int
	)
	{
		super.onLayout(changed, left, top, right, bottom)
		
		this.textPosition = (this.bottom - this.height * (1f - this.ticksWeight)) - this.textPadding
		this.textSizeOptimum = "0123456789".getOptimalTextSize(
			this.textSize,
			this.textPosition,
			this.painter
		)
		this.textHalfPadding = this.textPadding / 2f
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		for(i in 0 until this.barCount)
		{
			this.drawBar(canvas!!, i)
		}
	}
	
	private fun drawBar(canvas: Canvas, index: Int)
	{
		val label = (index + 1).toString()
		val left = this.factorizedWidth * index
		val right = left + this.factorizedWidth
		val halfTickThickness = this.ticksThickness / 2f
		val bounds = canvas.clipBounds.toRectF()
		
		bounds.set(
			left,
			bounds.top,
			right,
			bounds.bottom
		)
		
		val points = mutableListOf<Float>()
		var count = this.quantization.count
		
		if(this.quantization.mode == QuantizeUtils.QuantizeMode.Dotted)
		{
			count -= 1
		}
		
		for(i in 0 until count)
		{
			val height = when(i)
			{
				0 -> bounds.top
				else -> bounds.height() * this.ticksWeight
			}
			
			val x = bounds.width() * (i * this.quantization.value) + halfTickThickness
			
			points.add(bounds.left + x)
			points.add(height)
			points.add(bounds.left + x)
			points.add(bounds.bottom)
		}
		
		this.painter.color = this.ticksColor
		this.painter.strokeWidth = this.ticksThickness
		
		canvas.drawLines(points.toFloatArray(), this.painter)
		
		this.painter.color = this.textColor
		this.painter.textSize = this.textSizeOptimum
		this.painter.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
		
		canvas.drawText(
			label,
			bounds.left + this.textMargin,
			textPosition + this.textHalfPadding,
			this.painter
		)
	}
}