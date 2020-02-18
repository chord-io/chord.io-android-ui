package io.chord.ui.fragments.track

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.clients.models.Track
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.components.ListClickListener
import io.chord.ui.components.ListViewHolder
import io.chord.ui.components.TrackControl
import io.chord.ui.extensions.getChildOfType
import io.chord.ui.models.tracks.TrackListItemViewModel
import io.chord.ui.utils.ColorUtils
import io.chord.ui.utils.RippleDrawableUtils

class TrackListItemViewHolder(
	override val view: View
) : ListViewHolder<Track, TrackListItemViewModel>
{
	private var _trackControlMaster: TrackControl? = null
	private lateinit var _binding: TrackListItemBinding
	
	override var model: Track
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
	
	override fun bind(model: TrackListItemViewModel, listener: ListClickListener<Track>)
	{
		this._binding = DataBindingUtil.bind(this.view)!!
		this._binding.track = model
		
		this._binding.layout.setOnClickListener {
			listener.onItemClicked(this.model)
		}
		
		this.setBackgroundColor(model.model.color)
	}
	
	override fun unbind()
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
		val textColorSecondary = resources.getColor(R.color.textColorSecondary, theme)
		val backgroundPrimary = resources.getColor(R.color.backgroundPrimary, theme)
		val colorAccent = resources.getColor(R.color.colorAccent, theme)
		val textColor = ColorUtils.readableColor(
			color,
			textColorSecondary,
			backgroundPrimary
		)
		val rippleColor = if(textColor == textColorSecondary)
		{
			colorAccent
		}
		else
		{
			textColor
		}
		this._binding.layout.background = RippleDrawableUtils.create(
			color,
			rippleColor,
			RippleDrawableUtils.getColorDrawableFromColor(color)
		)
		this._binding.name.setTextColor(textColor)
	}
}