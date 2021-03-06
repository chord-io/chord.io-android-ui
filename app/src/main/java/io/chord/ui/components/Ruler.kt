package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toRectF
import io.chord.R
import io.chord.ui.behaviors.BarBehavior
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.QuantizeBehavior
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.extensions.alignCenter
import io.chord.ui.extensions.findOptimalTextSize
import io.chord.ui.extensions.getTextBounds
import io.chord.ui.utils.QuantizeUtils

class Ruler : View, Zoomable, Quantifiable, Bindable, Countable
{
	private var textSizeOptimum: Float = -1f
	private var textPosition: Float = -1f
	private var textHalfPadding: Float = -1f
	private val zoomBehavior: ZoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val quantizeBehavior = QuantizeBehavior()
	private val barBehavior = BarBehavior()
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
			this.zoomBehavior.widthAnimator.duration = value
		}
	
	var defaultWidth: Float
		get() = this._defaultWidth
		set(value) {
			this._defaultWidth = value
			this.quantizeBehavior.segmentLength = value
			this.setZoomFactor(
				ViewOrientation.Horizontal,
				this.zoomBehavior.getFactorWidth(),
				true
			)
			this.requestLayout()
			this.invalidate()
		}
	
	var ticksColor: Int
		get() = this._ticksColor
		set(value) {
			this._ticksColor = value
			this.requestLayout()
			this.invalidate()
		}
	
	var textColor: Int
		get() = this._textColor
		set(value) {
			this._textColor = value
			this.requestLayout()
			this.invalidate()
		}
	
	var ticksThickness: Float
		get() = this._ticksThickness
		set(value) {
			this._ticksThickness = value
			this.quantizeBehavior.offset = value / 2f
			this.requestLayout()
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
			this.requestLayout()
			this.invalidate()
		}
	
	var textSize: Float
		get() = this._textSize
		set(value) {
			this._textSize = value
			this.requestLayout()
			this.invalidate()
		}
	
	var textMargin: Float
		get() = this._textMargin
		set(value) {
			this._textMargin = value
			this.requestLayout()
			this.invalidate()
		}
	
	var textPadding: Float
		get() = this._textPadding
		set(value) {
			this._textPadding = value
			this.requestLayout()
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
		
		this._zoomDuration = typedArray.getInteger(
			R.styleable.Ruler_cio_rl_zoomDuration,
			this.resources.getInteger(R.integer.ruler_zoom_duration)
		).toLong()
		
		this._defaultWidth = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_defaultWidth,
			this.resources.getDimension(R.dimen.ruler_default_width)
		)
		
		this._ticksColor = typedArray.getColor(
			R.styleable.Ruler_cio_rl_ticksColor,
			this.resources.getColor(R.color.textColorPrimary, theme)
		)
		
		this._textColor = typedArray.getColor(
			R.styleable.Ruler_cio_rl_textColor,
			this.resources.getColor(R.color.textColorPrimary, theme)
		)
		
		this._ticksThickness = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_ticksThickness,
			this.resources.getDimension(R.dimen.ruler_tick_thickness)
		)
		
		this._ticksWeight = typedArray.getFloat(
			R.styleable.Ruler_cio_rl_ticksWeight,
			this.resources.getInteger(R.integer.ruler_ticks_wieght) / 100f
		)
		
		this._textSize = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textSize,
			this.resources.getDimension(R.dimen.ruler_text_size)
		)
		
		this._textMargin = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textMargin,
			this.resources.getDimension(R.dimen.ruler_text_margin)
		)
		
		this._textPadding = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_textPadding,
			this.resources.getDimension(R.dimen.ruler_text_padding)
		)
		
		typedArray.recycle()
		
		this.zoomBehavior.widthAnimator.duration = this.zoomDuration
		
		this.zoomBehavior.onEvaluateWidth = this::defaultWidth
		this.zoomBehavior.onMeasureWidth = {
			this.quantizeBehavior.segmentLength = this.zoomBehavior.factorizedWidth
			this.requestLayout()
			this.invalidate()
		}
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
	}
	
	override fun setQuantization(quantization: QuantizeUtils.Quantization)
	{
		this.quantizeBehavior.quantization = quantization
		this.requestLayout()
		this.invalidate()
	}
	
	override fun setCounter(counter: () -> List<Int>)
	{
		this.barBehavior.onCount = counter
		this.requestLayout()
		this.invalidate()
	}
	
	override fun onMeasure(
		widthMeasureSpec: Int,
		heightMeasureSpec: Int
	)
	{
		val count = this.barBehavior.count()
		
		val width = if(count == 0)
		{
			MeasureSpec.getSize(widthMeasureSpec)
		}
		else
		{
			this.quantizeBehavior.segmentCount = count
			this.quantizeBehavior.segmentLength = this.zoomBehavior.factorizedWidth
			this.quantizeBehavior.offset = this._ticksThickness / 2f
			this.quantizeBehavior.generate()
			
			val viewport = MeasureSpec.getSize(widthMeasureSpec)
			val content = this.zoomBehavior.factorizedWidth.toInt() * count
			
			if(content < viewport)
			{
				viewport
			}
			else
			{
				content
			}
		}
		
		val height = MeasureSpec.getSize(heightMeasureSpec)
		this.setMeasuredDimension(width, height)
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
		this.textSizeOptimum = "0123456789".findOptimalTextSize(
			this.textSize,
			this.textPosition,
			{bounds -> bounds.height()},
			this.painter
		)
		this.textHalfPadding = this.textPadding / 2f
	}
	
	override fun onDraw(canvas: Canvas)
	{
		if(this.barBehavior.count() == 0)
		{
			this.drawEmpty(canvas)
			return
		}
		
		val points = mutableListOf<Float>()
		
		for(i in 0 until this.quantizeBehavior.segmentCount)
		{
			this.drawBar(canvas, points, i)
		}
		
		this.painter.color = this.ticksColor
		this.painter.strokeWidth = this.ticksThickness
		
		canvas.drawLines(points.toFloatArray(), this.painter)
	}
	
	private fun drawEmpty(canvas: Canvas)
	{
		val label = "..."
		val height = label.getTextBounds(this.painter).height().toFloat()
		val bounds = Rect(canvas.clipBounds)
		val position = label.alignCenter(bounds.centerX(), 0, this.painter)
		
		this.painter.color = this.textColor
		this.painter.textSize = this.textSize
		
		canvas.drawText(
			label,
			position.x,
			bounds.centerY() + height / 2f,
			this.painter
		)
	}
	
	private fun drawBar(canvas: Canvas, pointsToDraw: MutableList<Float>, index: Int)
	{
		val label = (index + 1).toString()
		val points = this.quantizeBehavior.points[index]
		val bounds = canvas.clipBounds.toRectF()
		
		points.indices.forEach { i ->
			val height = when(i)
			{
				0 -> bounds.top
				else -> bounds.height() * this.ticksWeight
			}
			
			val x = points[i]
			
			pointsToDraw.add(x)
			pointsToDraw.add(height)
			pointsToDraw.add(x)
			pointsToDraw.add(bounds.bottom)
		}
		
		this.painter.color = this.textColor
		this.painter.textSize = this.textSizeOptimum
		this.painter.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

		canvas.drawText(
			label,
			points.first() + this.textMargin,
			textPosition + this.textHalfPadding,
			this.painter
		)
	}
}