package io.chord.ui.models.tracks

import androidx.databinding.BaseObservable
import io.chord.clients.models.Track
import io.chord.ui.components.ListViewModel

class TrackListItemViewModel(
	val model: Track
) : BaseObservable(), ListViewModel
{
	var name: String = model.name
	var color: Int = model.color
	
	fun toModel(): Track
	{
		this.model.name = this.name
		this.model.color = this.color
		return this.model
	}
	
	fun fromModel(model: Track)
	{
		this.name = model.name
		this.color = model.color
	}
}