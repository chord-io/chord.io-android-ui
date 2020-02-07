package io.chord.ui.components

import android.content.Context
import android.database.DataSetObserver
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import io.chord.R
import io.chord.clients.models.Track
import io.chord.databinding.TrackListDialogFormBinding
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.dialogs.cudc.CudcFormOperationDialogFragment
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.cudc.CudcOperationInformation
import io.chord.ui.dialogs.cudc.CudcSelectOperationDialogFragment
import io.chord.ui.models.TrackDialogFormViewModel
import java.util.*

class TrackList : LinearLayout, Zoomable, TrackListClickListener
{
	private class TrackListDataSetObserver(
		private val trackList: TrackList
	) : DataSetObserver()
	{
		override fun onChanged()
		{
			this.trackList.populateLayout()
		}
		
		override fun onInvalidated()
		{
			this.trackList.populateLayout()
		}
	}
	
	private val zoomBehavior: ZoomBehavior = ZoomBehavior()
	private val bindableBehavior = BindableBehavior(this)
	private val divider: ShapeDrawable = ShapeDrawable()
	private val adapter: TrackListAdapter = TrackListAdapter(
		this.context,
		{
			val holder = TrackListItem(it)
			holder.trackControlMaster = this.trackControlMaster
			holder
		},
		this
	)
	
	lateinit var trackControlMaster: TrackControl
	
	private var _zoomDuration: Long = -1
	private var _dividerColor: Int = -1
	private var _dividerThickness: Float = -1f
	private var _rowHeight: Float = -1f
	private var _rowPadding: Float = -1f
	
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
			this.invalidateDividerDrawable()
		}
	
	var dividerThickness: Float
		get() = this._dividerThickness
		set(value) {
			this._dividerThickness = value
			this.invalidateDividerDrawable()
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
		}
	
	var rowPadding: Float
		get() = this._rowPadding
		set(value) {
			this._rowPadding = value
			this.requestLayout()
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
			attrs, R.styleable.TrackList, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._zoomDuration = typedArray.getInteger(
			R.styleable.TrackList_cio_tl_zoomDuration,
			this.resources.getInteger(R.integer.track_list_zoom_duration)
		).toLong()
		
		this._dividerColor = typedArray.getColor(
			R.styleable.TrackList_cio_tl_dividerColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._dividerThickness = typedArray.getDimension(
			R.styleable.TrackList_cio_tl_dividerThickness,
			this.resources.getDimension(R.dimen.track_list_divider_thickness)
		)
		
		this._rowHeight = typedArray.getDimension(
			R.styleable.TrackList_cio_tl_rowHeight,
			this.resources.getDimension(R.dimen.track_list_row_height)
		)
		
		this._rowPadding = typedArray.getDimension(
			R.styleable.TrackList_cio_tl_rowPadding,
			this.resources.getDimension(R.dimen.track_list_row_padding)
		)
		
		typedArray.recycle()
		
		this.orientation = VERTICAL
		
		this.adapter.registerDataSetObserver(TrackListDataSetObserver(this))
		
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		
		this.zoomBehavior.onEvaluateHeight = this::rowHeight
		this.zoomBehavior.onMeasureHeight = this::onMeasureChange
		
		this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
			this.invalidateDividerDrawable()
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
	
	fun add(track: Track)
	{
		this.adapter.add(track)
	}
	
	fun remove(track: Track)
	{
		this.adapter.remove(track)
	}
	
	private fun invalidateDividerDrawable()
	{
		this.divider.intrinsicHeight = this.dividerThickness.toInt()
		this.divider.setColorFilter(this.dividerColor, PorterDuff.Mode.SRC)
		this.dividerDrawable = this.divider
	}
	
	private fun populateLayout()
	{
		this.adapter.recycleViews()
		this.removeAllViews()
		
		val height = this.zoomBehavior.factorizedHeight.toInt()
		val padding = this.rowPadding.toInt()
		val count = this.adapter.count
		
		for(index in 0 until count)
		{
			val view = this.adapter.getView(
				index,
				null,
				this
			)
			val holder = this.adapter.getViewHolder(view)
			holder.trackControlMaster = this.trackControlMaster
			this.adjustItemViewDimension(holder, height, padding)
			this.addView(view)
		}
	}
	
	private fun onMeasureChange()
	{
		val count = this.adapter.count
		
		if(count == 0)
		{
			return
		}
		
		val height = this.zoomBehavior.factorizedHeight.toInt()
		val padding = this.rowPadding.toInt()
		
		for(index in 0 until count)
		{
			val view = this.getChildAt(index)
			val holder = this.adapter.getViewHolder(view)
			this.adjustItemViewDimension(holder, height, padding)
		}
		
		this.requestLayout()
	}
	
	private fun adjustItemViewDimension(holder: TrackListItem, height: Int, padding: Int)
	{
		holder.view.setPadding(padding, padding, padding, padding)
		holder.view.layoutParams.height = height
	}
	
	override fun setZoomFactor(
		orientation: ViewOrientation,
		factor: Float,
		animate: Boolean
	)
	{
		if(orientation == ViewOrientation.Vertical)
		{
			this.zoomBehavior.setFactorHeight(factor, animate)
		}
	}
	
	override fun onItemClicked(item: TrackListItem)
	{
	}
	
	override fun onItemLongClicked(item: TrackListItem): Boolean
	{
		// TODO Factorize cudc workflow
		
		val fragmentManager = (this.context as AppCompatActivity).supportFragmentManager
		val dialogFragment =
			CudcSelectOperationDialogFragment(
				EnumSet.of(
					CudcOperation.UPDATE,
					CudcOperation.DELETE,
					CudcOperation.CLONE
				)
			)
		
		dialogFragment.onDeleteSelectedListener = { this.delete(item) }
		dialogFragment.onUpdateSelectedListener = { this.update(item) }
		dialogFragment.onCloneSelectedListener = { this.clone(item) }
		
		dialogFragment.show(fragmentManager, "fragment_track_list_cudc_dialog")
		return true
	}
	
	private fun delete(item: TrackListItem)
	{
		this.adapter.remove(item.model)
	}
	
	private fun update(item: TrackListItem)
	{
		val fragmentManager = (this.context as AppCompatActivity).supportFragmentManager
		val dialogFragment =
			CudcFormOperationDialogFragment<TrackListDialogFormBinding>(
				CudcOperationInformation(
					CudcOperation.UPDATE,
					this.resources.getString(R.string.track_list_entity_name)
				),
				R.layout.track_list_dialog_form
			)
		
		dialogFragment.onViewModelBinding = {
			it.track = TrackDialogFormViewModel(item.model)
		}
		
		dialogFragment.onLayoutUpdatedListener = {
			val track = it.track!!.toModel()
			this.adapter.update(track)
			dialogFragment.validate()
		}
		
		dialogFragment.show(fragmentManager, "fragment_track_list_update_form")
	}
	
	private fun clone(item: TrackListItem)
	{
		val trackToClone = item.model.copy()
		
		val fragmentManager = (this.context as AppCompatActivity).supportFragmentManager
		val dialogFragment =
			CudcFormOperationDialogFragment<TrackListDialogFormBinding>(
				CudcOperationInformation(
					CudcOperation.UPDATE,
					this.resources.getString(R.string.track_list_entity_name)
				),
				R.layout.track_list_dialog_form
			)
		
		dialogFragment.onViewModelBinding = { binding ->
			binding.track = TrackDialogFormViewModel(trackToClone)
		}
		
		dialogFragment.onLayoutUpdatedListener = { binding ->
			val track = binding.track!!.toModel()
			val count = this.adapter.items.count {
				it.name == track.name
			}
			
			if(count == 0)
			{
				this.adapter.add(track)
				dialogFragment.validate()
			}
			else
			{
				binding.nameLayout.isErrorEnabled = true
				binding.nameLayout.error = "Name track already exist"
				dialogFragment.invalidate()
			}
		}
		
		dialogFragment.show(fragmentManager, "fragment_track_list_clone_form")
	}
}