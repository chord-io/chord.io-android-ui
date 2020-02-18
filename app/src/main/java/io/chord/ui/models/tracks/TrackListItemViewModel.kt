package io.chord.ui.models.tracks

import androidx.databinding.BaseObservable
import io.chord.clients.models.Track
import io.chord.ui.components.ListViewModel

class TrackListItemViewModel(
	val model: Track
) : BaseObservable(), ListViewModel
{
	// TODO normalize toModel/fromModel
	var name: String = model.name
	
	fun toModel(): Track
	{
		this.model.name = this.name
		return this.model
	}
	
	fun fromModel(model: Track)
	{
		this.name = model.name
	}
}