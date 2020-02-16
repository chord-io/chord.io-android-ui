package io.chord.ui.models.tracks

import io.chord.clients.models.DrumTrack
import io.chord.clients.models.Track

class DrumTrackViewModel : MidiTrackViewModel()
{
	var drumMap: String = ""
	
	override fun toModel(): Track
	{
		val model = super.toModel() as DrumTrack
		model.drumMap = this.drumMap
		return model
	}
	
	override fun fromModel(model: Track)
	{
		super.fromModel(model)
		
		if(model !is DrumTrack)
		{
			throw ClassCastException("Expected DrumTrack type as model")
		}
		
		this.fromModel(this.model as DrumTrack)
	}
	
	private fun fromModel(model: DrumTrack)
	{
		this.drumMap = model.drumMap
	}
}