package io.chord.ui.components

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.models.TrackListItemViewModel

class TrackListItem(
	val view: View
)
{
	private lateinit var binding: TrackListItemBinding
	private var initialNameViewTextSize: Float = 0f
	
	fun bind(model: TrackListItemViewModel)
	{
		this.binding = DataBindingUtil.bind(this.view)!!
		this.binding.track = model
		
		this.initialNameViewTextSize = this.binding.name.textSize
	}
}