package io.chord.ui.models.tracks

import io.chord.clients.models.DrumTrack

class DrumTrackViewModel : MidiTrackViewModel()
{
	var drumMap: String = ""

	override fun toModel(): DrumTrack
	{
		return DrumTrack(
			this.name,
			this.color,
			this.channel,
			this.drumMap
		)
	}

	fun fromModel(model: DrumTrack)
	{
		this.drumMap = model.drumMap
		super.fromModel(model)
	}
}