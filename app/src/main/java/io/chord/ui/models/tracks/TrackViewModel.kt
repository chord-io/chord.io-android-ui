package io.chord.ui.models.tracks

import androidx.databinding.BaseObservable
import io.chord.clients.models.Track

abstract class TrackViewModel : BaseObservable()
{
	var name: String = ""

	var color: Int = 0
	
	abstract fun toModel(): Track

	fun fromModel(model: Track) {
		this.name = model.name
		this.color = model.color
	}
}