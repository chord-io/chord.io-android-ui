package io.chord.ui.components

import android.content.Context
import android.database.DataSetObserver
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

class Sequencer : View, Zoomable, Listable<Track>
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
	
	private var _zoomDuration: Long = -1
	private var _dividerColor: Int = -1
	private var _dividerThickness: Float = -1f
	private var _rowHeight: Float = -1f
	private var _rowPadding: Float = -1f
	private var _barWidth: Float = -1f
	private var _ticksThickness: Float = -1f
	
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
		
		typedArray.recycle()
		
		this.zoomBehavior.widthAnimator.duration = this.zoomDuration
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		this.zoomBehavior.onEvaluateHeight = {
			this.rowHeight + this.dividerThickness
		}
		this.zoomBehavior.onMeasureHeight = {}
		this.zoomBehavior.onEvaluateWidth = this::barWidth
		this.zoomBehavior.onMeasureWidth = {}
		
		this.barBehavior.onCount = {
			ProjectManager.getCurrent()!!.tracks.flatMap {
				it.themes
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
		val width = this.zoomBehavior.factorizedWidth * this.barBehavior.count()
		val height = this.zoomBehavior.factorizedHeight * this.tracks.size - this.dividerThickness
		this.setMeasuredDimension(width.toInt(), height.toInt())
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
}