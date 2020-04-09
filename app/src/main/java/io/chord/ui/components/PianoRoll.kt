package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.graphics.toRectF
import io.chord.R
import io.chord.ui.behaviors.BarBehavior
import io.chord.ui.behaviors.QuantizeBehavior
import io.chord.ui.behaviors.SurfaceGestureBehavior
import io.chord.ui.extensions.toTransparent
import io.chord.ui.utils.QuantizeUtils

class PianoRoll : KeyboardList, Quantifiable, Countable
{
	private class BarTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	
	private val quantizeBehavior = QuantizeBehavior()
	private val barBehavior = BarBehavior()
	private val gestureBehavior = SurfaceGestureBehavior(this.context)
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private var _ticksColor: Int = -1
	private var _ticksThickness: Float = -1f
	private var _barWidth: Float = -1f
	
	var ticksColor: Int
		get() = this._ticksColor
		set(value) {
			this._ticksColor = value
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
	
	var barWidth: Float
		get() = this._barWidth
		set(value) {
			this._barWidth = value
			this.quantizeBehavior.segmentLength = value
			this.setZoomFactor(
				ViewOrientation.Horizontal,
				this.zoomBehavior.getFactorWidth(),
				true
			)
			this.setZoomFactor(
				ViewOrientation.Vertical,
				this.zoomBehavior.getFactorHeight(),
				true
			)
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
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.PianoRoll, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._ticksColor = typedArray.getColor(
			R.styleable.PianoRoll_cio_pr_ticksColor,
			this.resources.getColor(R.color.borderColorPrimary, theme)
		)
		
		this._ticksThickness = typedArray.getDimension(
			R.styleable.PianoRoll_cio_pr_ticksThickness,
			this.resources.getDimension(R.dimen.piano_roll_ticks_thickness)
		)
		
		this._barWidth = typedArray.getDimension(
			R.styleable.PianoRoll_cio_pr_barWidth,
			this.resources.getDimension(R.dimen.piano_roll_bar_width)
		)
		
		typedArray.recycle()
		
		this.zoomBehavior.onEvaluateWidth = this::barWidth
		this.zoomBehavior.onMeasureWidth = {
			this.quantizeBehavior.segmentLength = this.zoomBehavior.factorizedWidth
			this.requestLayout()
			this.invalidate()
		}
	}
	
	override fun onFinishInflate()
	{
		super.onFinishInflate()
		this.orientation = ViewOrientation.Vertical
		this.whiteKeyColor = this.whiteKeyColor.toTransparent(0.1f)
		this.blackKeyColor = this.blackKeyColor.toTransparent(0.1f)
		this.strokeColor = this.strokeColor.toTransparent(0.1f)
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
	
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
	{
		this.quantizeBehavior.generate()
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
		
		super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec)
		
		val height = this.measuredHeight
		
		this.setMeasuredDimension(width, height)
	}
	
	override fun dispatchDraw(canvas: Canvas)
	{
		super.dispatchDraw(canvas)
		
		val points = mutableListOf<Float>()
		
		for(i in 0 until this.quantizeBehavior.segmentCount)
		{
			this.drawBar(canvas, points, i)
		}
		
		this.painter.color = this.ticksColor
		this.painter.strokeWidth = this.ticksThickness
		
		canvas.drawLines(points.toFloatArray(), this.painter)
	}
	
	private fun drawBar(canvas: Canvas, pointsToDraw: MutableList<Float>, index: Int)
	{
		val points = this.quantizeBehavior.points[index]
		val bounds = canvas.clipBounds.toRectF()
		val left = this.quantizeBehavior.segmentLength * index
		val right = left + this.quantizeBehavior.segmentLength
		
		this.gestureBehavior.surfaces.add(
			BarTouchSurface(
				RectF(
					left,
					bounds.top,
					right,
					bounds.bottom
				),
				index
			)
		)
		
		points.indices.forEach { i ->
			val x = points[i]
			pointsToDraw.add(x)
			pointsToDraw.add(bounds.top)
			pointsToDraw.add(x)
			pointsToDraw.add(bounds.bottom)
		}
	}
}