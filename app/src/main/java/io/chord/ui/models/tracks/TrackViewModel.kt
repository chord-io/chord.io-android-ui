package io.chord.ui.models.tracks

import androidx.databinding.BaseObservable
import io.chord.clients.models.Track

abstract class TrackViewModel: BaseObservable()
{
	var name: String = ""

	var color: Int = 0
	
	protected lateinit var model: Track
	
	open fun toModel(): Track
	{
		this.model.name = this.name
		this.model.color = this.color
		return this.model
	}

	open fun fromModel(model: Track) {
		this.name = model.name
		this.color = model.color
		this.model = model
	}
	
	fun onColorChanged(color: Int)
	{
		this.color = color
	}
}