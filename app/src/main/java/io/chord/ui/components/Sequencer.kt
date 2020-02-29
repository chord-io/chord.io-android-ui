package io.chord.ui.components

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
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
import io.chord.ui.utils.QuantizeUtils

class Sequencer : View, Zoomable, Listable<Track>, Quantifiable
{
	private class SequencerDataSetObserver(
		private val sequencer: Sequencer
	) : DataSetObserver()
	{
		override fun onChanged()
		{
			this.sequencer.requestLayout()
			this.sequencer.invalidate()
		}
		
		override fun onInvalidated()
		{
			this.sequencer.requestLayout()
			this.sequencer.invalidate()
		}
	}
	
	private val zoomBehavior: ZoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val quantizeBehavior = QuantizeBehavior()
	private val barBehavior = BarBehavior()
	private var tracks: List<Track> = listOf()
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
	
	private var _zoomDuration: Long = -1
	private var _dividerColor: Int = -1
	private var _textColor: Int = -1
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
			this.invalidate()
		}
	
	var textColor: Int
		get() = this._textColor
		set(value) {
			this._textColor = value
			this.invalidate()
		}
	
	var dividerThickness: Float
		get() = this._dividerThickness
		set(value) {
			this._dividerThickness = value
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
			this.invalidate()
		}
	
	var rowPadding: Float
		get() = this._rowPadding
		set(value) {
			this._rowPadding = value
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
			this.invalidate()
		}
	
	var ticksThickness: Float
		get() = this._ticksThickness
		set(value) {
			this._ticksThickness = value
			this.quantizeBehavior.offset = value / 2f
			this.invalidate()
		}
	
	var textSize: Float
		get() = this._textSize
		set(value) {
			this._textSize = value
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
		this.zoomBehavior.onEvaluateHeight = {
			this.rowHeight
		}
		this.zoomBehavior.onMeasureHeight = {
			this.requestLayout()
			this.invalidate()
		}
		this.zoomBehavior.onEvaluateWidth = this::barWidth
		this.zoomBehavior.onMeasureWidth = {}
		
		this.barBehavior.onCount = {
			ProjectManager.getCurrent()!!.tracks.flatMap {
				it.entries
			}
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
			(this.zoomBehavior.factorizedWidth * count).toInt()
		}
		
		val height = if(this.tracks.isEmpty())
		{
			MeasureSpec.getSize(heightMeasureSpec)
		}
		else
		{
			val divider = (this.tracks.size - 1) * this.dividerThickness.toInt()
			val lanes = this.zoomBehavior.factorizedHeight.toInt() * this.tracks.size
			lanes + divider
		}
		
		this.setMeasuredDimension(width, height)
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
		this.tracks = dataset
		this.requestLayout()
		this.invalidate()
	}
	
	override fun setQuantization(quantization: QuantizeUtils.Quantization)
	{
		this.quantizeBehavior.quantization = quantization
		this.requestLayout()
		this.invalidate()
	}
	
	override fun onDraw(canvas: Canvas)
	{
		if(this.barBehavior.count() == 0)
		{
			this.drawEmpty(canvas)
			return
		}
		
		this.drawLane(canvas).forEach {
			this.painter.color = it.second
			canvas.drawRect(it.first, this.painter)
		}
	}
	
	private fun drawLane(canvas: Canvas): List<Pair<Rect, Int>>
	{
		val lanes = mutableListOf<Pair<Rect, Int>>()
		val bounds = canvas.clipBounds
		this.tracks.indices.forEach { index ->
			val divider = if(index > 0)
			{
				this.dividerThickness
			}
			else
			{
				0f
			}
			val height = this.zoomBehavior.factorizedHeight
			val top = (height * index + divider).toInt()
			val bottom = (top + height + divider).toInt()
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
}