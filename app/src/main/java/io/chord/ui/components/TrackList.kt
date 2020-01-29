package io.chord.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.database.DataSetObserver
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.setPadding
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import io.chord.R
import io.chord.clients.models.Track
import io.chord.ui.utils.ViewUtils

class TrackList : LinearLayout, Zoomable
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
	
	private var zoomfactor: Float = 0.3f
	private var factorizedHeight: Float = -1f
	private var factorAnimator: ValueAnimator = ValueAnimator()
	private val divider: ShapeDrawable = ShapeDrawable()
	private val adapter: TrackListAdapter = TrackListAdapter(this.context)
	
	private var _dividerColor: Int = -1
	private var _dividerThickness: Float = -1f
	private var _rowHeight: Float = -1f
	private var _rowPadding: Float = -1f
	
	var dividerColor: Int
		get() = this._dividerColor
		set(value) {
			this._dividerColor = value
		}
	
	var dividerThickness: Float
		get() = this._dividerThickness
		set(value) {
			this._dividerThickness = value
		}
	
	var rowHeight: Float
		get() = this._rowHeight
		set(value) {
			this._rowHeight = value
			this.setZoomFactor(ViewOrientation.Vertical, this.zoomfactor, true)
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
		
		this.dividerColor = typedArray.getColor(
			R.styleable.TrackList_cio_tl_dividerColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this.dividerThickness = typedArray.getDimension(
			R.styleable.TrackList_cio_tl_dividerThickness,
			this.resources.getDimension(R.dimen.track_list_divider_thickness)
		)
		
		this.rowHeight = typedArray.getDimension(
			R.styleable.TrackList_cio_tl_rowHeight,
			this.resources.getDimension(R.dimen.track_list_row_height)
		)
		
		this.rowPadding = typedArray.getDimension(
			R.styleable.TrackList_cio_tl_rowPadding,
			this.resources.getDimension(R.dimen.track_list_row_padding)
		)
		
		typedArray.recycle()
		
		this.orientation = LinearLayout.VERTICAL
		
		this.adapter.registerDataSetObserver(TrackListDataSetObserver(this))
		
		// TODO : set interpolator on another place
		this.factorAnimator.interpolator = FastOutSlowInInterpolator()
		// TODO : set as an attribute view
		this.factorAnimator.duration = this.resources.getInteger(R.integer.track_list_zoom_duration).toLong()
		this.factorAnimator.addUpdateListener { animator ->
			val factor = animator.animatedValue as Float
			this.internalSetZoomFactor(factor)
		}
		this.factorAnimator.doOnEnd {
			val animator = it as ValueAnimator
			val factor = animator.animatedValue as Float
			this.internalSetZoomFactor(factor)
		}
		
		this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
			this.invalidateDividerDrawable()
		}
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
		this.removeAllViews()
		
		val height = this.factorizedHeight.toInt()
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
			this.adjustItemViewDimension(holder, height, padding)
			this.addView(view)
		}
	}
	
	private fun adjustItemViewDimension(holder: TrackListItem, height: Int, padding: Int)
	{
		holder.view.setPadding(padding, padding, padding, padding)
		holder.view.layoutParams.height = height
	}
	
	private fun internalSetZoomFactor(factor: Float)
	{
		this.zoomfactor = factor
		this.factorizedHeight = this.rowHeight * this.zoomfactor
		
		val height = this.factorizedHeight.toInt()
		val padding = this.rowPadding.toInt()
		val count = this.adapter.count
		
		if(count == 0)
		{
			return
		}
		
		for(index in 0 until count)
		{
			val view = this.getChildAt(index)
			val holder = this.adapter.getViewHolder(view)
			this.adjustItemViewDimension(holder, height, padding)
		}
		
		this.requestLayout()
	}
	
	override fun setZoomFactor(
		orientation: ViewOrientation,
		factor: Float,
		animate: Boolean
	)
	{
		if(orientation == ViewOrientation.Vertical)
		{
			// TODO: factoryze behavior like  factor animator
			
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
}