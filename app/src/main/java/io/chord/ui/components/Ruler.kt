package io.chord.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.graphics.toRectF
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.chord.R
import io.chord.ui.utils.QuantizeUtils
import io.chord.ui.utils.ViewUtils


class Ruler : View, Zoomable, Quantifiable
{
	private var barCount: Int = 3
	private var zoomfactor: Float = 1f
	private var factorizedWidth: Float = -1f
	private var factorAnimator: ValueAnimator = ValueAnimator()
	private var quantization: QuantizeUtils.Quantization = QuantizeUtils.Quantization(
		QuantizeUtils.QuantizeValue.First,
		QuantizeUtils.QuantizeMode.Natural
	)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private var _defaultWidth: Float = -1f
	private var _ticksColor: Int = -1
	private var _textColor: Int = -1
	private var _ticksThickness: Float = -1f
	private var _textSize: Float = -1f
	private var _textMargin: Float = -1f
	
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
		
		this.textSize = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textSize,
			this.resources.getDimension(R.dimen.ruler_text_size)
		)
		
		this.textMargin = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textMargin,
			this.resources.getDimension(R.dimen.ruler_text_margin)
		)
		
		typedArray.recycle()
		
		// TODO : set interpolator on another place
		this.factorAnimator.interpolator = FastOutSlowInInterpolator()
		// TODO : set as an attribute view
		this.factorAnimator.duration = this.resources.getInteger(R.integer.ruler_zoom_duration).toLong()
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
	
	override fun onDraw(canvas: Canvas?)
	{
		for(i in 0 until this.barCount)
		{
			this.drawBar(canvas!!, i)
		}
	}
	
	private fun drawBar(canvas: Canvas, index: Int)
	{
		this.painter.color = this.ticksColor
		this.painter.strokeWidth = this.ticksThickness
		
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
		
		canvas.save()
		
		canvas.clipRect(bounds)
		
		for(i in 0..this.quantization.count)
		{
			val height = when(i)
			{
				0 -> bounds.top
				else -> bounds.height() * 0.5f
			}
			
			val x = bounds.width() * (i * this.quantization.value) + halfTickThickness
			
			canvas.drawLine(
				bounds.left + x,
				height,
				bounds.left + x,
				bounds.bottom,
				this.painter
			)
		}
		
		this.painter.color = this.textColor
		this.painter.textSize = this.textSize
		
		// TODO text position on first half height
		
		val textPosition = ViewUtils.getTextCentered(
			label,
			bounds.left.toInt(),
			bounds.centerY().toInt(),
			this.painter
		)
		
		var scaledTextMargin = this.textMargin / this.defaultWidth * bounds.width()
		
		if(scaledTextMargin > this.textMargin)
		{
			scaledTextMargin = this.textMargin
		}
		
		canvas.drawText(
			label,
			bounds.left + scaledTextMargin,
			textPosition.y,
			this.painter
		)
		
		canvas.restore()
	}
}