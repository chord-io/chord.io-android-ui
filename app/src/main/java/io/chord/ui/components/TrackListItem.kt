package io.chord.ui.components

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.clients.models.Track
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.extensions.getChildOfType
import io.chord.ui.models.TrackListItemViewModel

class TrackListItem(
	val view: View
)
{
	// TODO Generify
	private lateinit var _trackControlMaster: TrackControl
	private lateinit var _binding: TrackListItemBinding
	
	var model: Track
		get() = this._binding.track!!.model
		set(value) {
			this._binding.track!!.fromModel(value)
			this._binding.invalidateAll()
		}
	
	var trackControlMaster: TrackControl
		get() = this._trackControlMaster
		set(value) {
			this._trackControlMaster = value
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
		
		val control = this._binding
			.layout
			.getChildOfType<TrackControl>()
			.first()
		
		this._trackControlMaster.attach(control)
	}
	
	fun unbind()
	{
		this._binding
			.layout
			.getChildOfType<TrackControl>()
			.first()
			.selfDetach()
	}
}