package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import io.chord.R
import io.chord.clients.models.Track
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.BarBehavior
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.QuantizeBehavior
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.extensions.getTextBounds
import io.chord.ui.extensions.getTextCentered
import io.chord.ui.extensions.toTransparent
import io.chord.ui.gestures.SimpleOnGestureListener
import io.chord.ui.utils.QuantizeUtils

class Sequencer : View, Zoomable, Listable<Track>, Quantifiable, Modulable<EditorMode>
{
	private class SelectState(
		private val context: EditorState
	) : EditorState.State, SimpleOnGestureListener()
	{
		private var rectangle: EditorGesture.Rectangle? = null
		
		override fun onDown(event: MotionEvent): Boolean
		{
			this.rectangle = EditorGesture.Rectangle(
				PointF(
					event.x,
					event.y
				),
				PointF(
					event.x,
					event.y
				)
			)
			this.context.gesture.drawer.draw(this.rectangle!!.toSurface())
			this.context.gesture.intersect(this.rectangle!!.toSurface())
			return true
		}
		
		override fun onUp(event: MotionEvent): Boolean
		{
			this.rectangle = null
			this.context.gesture.drawer.clear()
			return true
		}
		
		override fun onMove(event: MotionEvent): Boolean
		{
			this.rectangle = EditorGesture.Rectangle(
				this.rectangle!!.a,
				PointF(
					event.x,
					event.y
				)
			)
			this.context.gesture.drawer.draw(this.rectangle!!.toSurface())
			this.context.gesture.intersect(this.rectangle!!.toSurface())
			return true
		}
	}
	
	private val zoomBehavior: ZoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val quantizeBehavior = QuantizeBehavior()
	private val barBehavior = BarBehavior()
	private val gesture = EditorGesture(this.context)
	private val stateContext = EditorState(this.gesture)
	private val tracks: MutableList<Track> = mutableListOf()
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private var _zoomDuration: Long = -1
	private var _dividerColor: Int = -1
	private var _textColor: Int = -1
	private var _ticksColor: Int = -1
	private var _dividerThickness: Float = -1f
	private var _rowHeight: Float = -1f
	private var _rowPadding: Float = -1f
	private var _barWidth: Float = -1f
	private var _ticksThickness: Float = -1f
	private var _textSize: Float = -1f
	
	var zoomDuration: Long
		get() = this._zoomDuration
		set(value) {
			this._zoomDuration = value
			this.zoomBehavior.heightAnimator.duration = value
		}
	
	var dividerColor: Int
		get() = this._dividerColor
		set(value) {
			this._dividerColor = value
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
	
	var ticksColor: Int
		get() = this._ticksColor
		set(value) {
			this._ticksColor = value
			this.requestLayout()
			this.invalidate()
		}
	
	var dividerThickness: Float
		get() = this._dividerThickness
		set(value) {
			this._dividerThickness = value
			this.requestLayout()
			this.invalidate()
		}
	
	var rowHeight: Float
		get() = this._rowHeight
		set(value) {
			this._rowHeight = value
			this.setZoomFactor(
				ViewOrientation.Vertical,
				this.zoomBehavior.getFactorHeight(),
				true
			)
			this.requestLayout()
			this.invalidate()
		}
	
	var rowPadding: Float
		get() = this._rowPadding
		set(value) {
			this._rowPadding = value
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
	
	var textSize: Float
		get() = this._textSize
		set(value) {
			this._textSize = value
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
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.Sequencer, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._zoomDuration = typedArray.getInteger(
			R.styleable.Sequencer_cio_sq_zoomDuration,
			this.resources.getInteger(R.integer.sequencer_zoom_duration)
		).toLong()
		
		this._dividerColor = typedArray.getColor(
			R.styleable.Sequencer_cio_sq_dividerColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._textColor = typedArray.getColor(
			R.styleable.Sequencer_cio_sq_textColor,
			this.resources.getColor(R.color.textColor, theme)
		)
		
		this._ticksColor = typedArray.getColor(
			R.styleable.Sequencer_cio_sq_ticksColor,
			this.resources.getColor(R.color.borderColor, theme)
		)
		
		this._dividerThickness = typedArray.getDimension(
			R.styleable.Sequencer_cio_sq_dividerThickness,
			this.resources.getDimension(R.dimen.sequencer_divider_thickness)
		)
		
		this._rowHeight = typedArray.getDimension(
			R.styleable.Sequencer_cio_sq_rowHeight,
			this.resources.getDimension(R.dimen.sequencer_row_height)
		)
		
		this._rowPadding = typedArray.getDimension(
			R.styleable.Sequencer_cio_sq_rowPadding,
			this.resources.getDimension(R.dimen.sequencer_row_padding)
		)
		
		this._barWidth = typedArray.getDimension(
			R.styleable.Sequencer_cio_sq_barWidth,
			this.resources.getDimension(R.dimen.sequencer_bar_width)
		)
		
		this._ticksThickness = typedArray.getDimension(
			R.styleable.Sequencer_cio_sq_ticksThickness,
			this.resources.getDimension(R.dimen.sequencer_tick_thickness)
		)
		
		this._textSize = typedArray.getDimension(
			R.styleable.Sequencer_cio_sq_textSize,
			this.resources.getDimension(R.dimen.sequencer_text_size)
		)
		
		typedArray.recycle()
		
		this.zoomBehavior.widthAnimator.duration = this.zoomDuration
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		this.zoomBehavior.onEvaluateWidth = this::barWidth
		this.zoomBehavior.onMeasureWidth = {
			this.quantizeBehavior.segmentLength = this.zoomBehavior.factorizedWidth
			this.requestLayout()
			this.invalidate()
		}
		this.zoomBehavior.onEvaluateHeight = {
			this.rowHeight
		}
		this.zoomBehavior.onMeasureHeight = {
			this.requestLayout()
			this.invalidate()
		}
		
		this.barBehavior.onCount = {
			ProjectManager.getCurrent()!!.tracks.flatMap {
				it.entries
			}
		}
		
		this.gesture.drawer.onInvalidate = {
			this.invalidate()
		}
		
		this.stateContext.modes[EditorMode.Select] = SelectState(this.stateContext)
		
		this.isClickable = true
		this.isFocusable = true
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
	
	override fun onMeasure(
		widthMeasureSpec: Int,
		heightMeasureSpec: Int
	)
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
		
		val height = if(this.tracks.isEmpty())
		{
			MeasureSpec.getSize(heightMeasureSpec)
		}
		else
		{
			val divider = (this.tracks.size - 1) * this.dividerThickness.toInt()
			val lanes = this.zoomBehavior.factorizedHeight.toInt() * this.tracks.size
			val viewport = MeasureSpec.getSize(heightMeasureSpec)
			val content = lanes + divider
			
			if(content < viewport)
			{
				viewport
			}
			else
			{
				content
			}
		}
		
		this.setMeasuredDimension(width, height)
	}
	
	override fun setMode(mode: EditorMode)
	{
		this.stateContext.setMode(mode)
	}
	
	override fun setZoomFactor(orientation: ViewOrientation, factor: Float, animate: Boolean)
	{
		if(orientation == ViewOrientation.Vertical)
		{
			this.zoomBehavior.setFactorHeight(factor, animate)
		}
		else if(orientation == ViewOrientation.Horizontal)
		{
			this.zoomBehavior.setFactorWidth(factor, animate)
		}
	}
	
	override fun setDataSet(dataset: List<Track>)
	{
		this.tracks.clear()
		this.tracks.addAll(dataset)
		this.requestLayout()
		this.invalidate()
	}
	
	override fun setQuantization(quantization: QuantizeUtils.Quantization)
	{
		this.quantizeBehavior.quantization = quantization
		this.requestLayout()
		this.invalidate()
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		return if(this.gesture.onTouchEvent(event))
		{
			true
		}
		else
		{
			super.onTouchEvent(event)
		}
	}
	
	override fun onDraw(canvas: Canvas)
	{
		this.gesture.surfaces.clear()
		
		if(this.barBehavior.count() == 0)
		{
			this.drawEmpty(canvas)
			return
		}
		
		canvas.getClipBounds(this.gesture.bounds)
		
		this.drawLane(canvas).forEach {
			this.painter.color = it.second
			canvas.drawRect(it.first, this.painter)
		}
		
		this.painter.color = this.dividerColor
		
		canvas.drawLines(this.drawDivider(canvas), this.painter)
		
		val points = mutableListOf<Float>()
		
		for(i in 0 until this.quantizeBehavior.segmentCount)
		{
			this.drawBar(canvas, points, i)
		}
		
		this.painter.color = this.ticksColor
		this.painter.strokeWidth = this.ticksThickness
		
		canvas.drawLines(points.toFloatArray(), this.painter)
		
		if(this.gesture.drawer.frame != null)
		{
			this.gesture.drawer.frame!!.draw(canvas)
		}
	}
	
	private fun drawEmpty(canvas: Canvas)
	{
		val label = "..."
		val height = label.getTextBounds(this.painter).height().toFloat()
		val bounds = Rect(canvas.clipBounds)
		val position = label.getTextCentered(bounds.centerX(), 0, this.painter)
		
		this.painter.color = this.textColor
		this.painter.textSize = this.textSize
		
		canvas.drawText(
			label,
			position.x,
			bounds.centerY() + height / 2f,
			this.painter
		)
	}
	
	private fun drawLane(canvas: Canvas): List<Pair<Rect, Int>>
	{
		val lanes = mutableListOf<Pair<Rect, Int>>()
		val bounds = canvas.clipBounds
		this.tracks.indices.forEach { index ->
			val divider = if(index > 0)
			{
				this.dividerThickness.toInt()
			}
			else
			{
				0
			}
			val height = this.zoomBehavior.factorizedHeight.toInt()
			val top = (height + divider) * index
			val bottom = top + height
			val track = this.tracks[index]
			val color = track.color.toTransparent(0.1f)
			val lane = Rect(
				bounds.left,
				top,
				bounds.right,
				bottom
			)
			lanes.add(lane to color)
		}
		return lanes
	}
	
	private fun drawDivider(canvas: Canvas): FloatArray
	{
		val points = mutableListOf<Float>()
		val bounds = canvas.clipBounds.toRectF()
		val divider = this.dividerThickness.toInt()
		val halfDivider = divider / 2f
		val height = this.zoomBehavior.factorizedHeight.toInt()
		
		for(index in 1 until this.tracks.size)
		{
			val point = if(points.isEmpty())
			{
				height.toFloat() + halfDivider
			}
			else
			{
				points.last() + divider + height
			}
			
			val y = point
			
			points.add(bounds.left)
			points.add(y)
			points.add(bounds.right)
			points.add(y)
		}
		
		return points.toFloatArray()
	}
	
	private fun drawBar(canvas: Canvas, pointsToDraw: MutableList<Float>, index: Int)
	{
		val points = this.quantizeBehavior.points[index]
		val bounds = canvas.clipBounds.toRectF()
		val left = this.quantizeBehavior.segmentLength * index
		val right = left + this.quantizeBehavior.segmentLength
		
		this.gesture.surfaces.add(EditorGesture.BarTouchSurface(
			RectF(
				left,
				bounds.top,
				right,
				bounds.bottom
			),
			index
		))
		
		points.indices.forEach { i ->
			val x = points[i]
			pointsToDraw.add(x)
			pointsToDraw.add(bounds.top)
			pointsToDraw.add(x)
			pointsToDraw.add(bounds.bottom)
		}
	}
}