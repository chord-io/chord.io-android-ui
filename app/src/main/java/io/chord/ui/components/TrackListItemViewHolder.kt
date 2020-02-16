package io.chord.ui.components

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.clients.models.Track
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.extensions.getChildOfType
import io.chord.ui.models.TrackListItemViewModel
import io.chord.ui.utils.ColorUtils
import io.chord.ui.utils.RippleDrawableUtils

class TrackListItemViewHolder(
	val view: View
)
{
	private var _trackControlMaster: TrackControl? = null
	private lateinit var _binding: TrackListItemBinding
	
	var model: Track
		get() = this._binding.track!!.model
		set(value) {
			this._binding.track!!.fromModel(value)
			this._binding.invalidateAll()
			this.setBackgroundColor(value.color)
		}
	
	var trackControlMaster: TrackControl
		get() = this._trackControlMaster!!
		set(value) {
			val control = this._binding
				.layout
				.getChildOfType<TrackControl>()
				.first()
			control.selfDetach()
			this._trackControlMaster = value
			this._trackControlMaster!!.attach(control)
		}
	
	fun bind(model: TrackListItemViewModel, listener: TrackListClickListener)
	{
		this._binding = DataBindingUtil.bind(this.view)!!
		this._binding.track = model
		
		this._binding.layout.setOnClickListener {
			listener.onItemClicked(this)
		}
		
		this._binding.layout.setOnLongClickListener {
			listener.onItemLongClicked(this)
		}
		
		this.setBackgroundColor(model.model.color)
	}
	
	fun unbind()
	{
		this._binding
			.layout
			.getChildOfType<TrackControl>()
			.first()
			.selfDetach()
	}
	
	private fun setBackgroundColor(color: Int)
	{
		val theme = this.view.context.theme
		val resources = this.view.resources
		this._binding.layout.background = RippleDrawableUtils.create(
			color,
			android.R.color.holo_red_dark,
			RippleDrawableUtils.getColorDrawableFromColor(color)
		)
		val textColor = ColorUtils.readableTextColor(
			color,
			resources.getColor(R.color.textColor, theme),
			resources.getColor(R.color.backgroundPrimary, theme)
		)
		this._binding.name.setTextColor(textColor)
	}
}