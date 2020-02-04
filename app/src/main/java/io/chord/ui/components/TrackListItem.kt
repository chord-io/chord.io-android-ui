package io.chord.ui.components

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.extensions.getChildOfType
import io.chord.ui.models.TrackListItemViewModel

class TrackListItem(
	val view: View
)
{
	lateinit var binding: TrackListItemBinding
	
	fun bind(model: TrackListItemViewModel, listener: TrackListClickListener)
	{
		this.binding = DataBindingUtil.bind(this.view)!!
		this.binding.track = model
		
		this.binding.layout.setOnClickListener {
			listener.onItemClicked(this)
		}
		
		this.binding.layout.setOnLongClickListener {
			listener.onItemLongClicked(this)
		}
		
		this.binding
			.layout
			.getChildOfType<TrackControl>()
			.first()
			.selfAttach()
	}
	
	fun unbind()
	{
		this.binding
			.layout
			.getChildOfType<TrackControl>()
			.first()
			.selfDetach()
	}
}