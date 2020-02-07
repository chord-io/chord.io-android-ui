package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Track

class TrackListItemViewModel(
	val model: Track
) : BaseObservable()
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