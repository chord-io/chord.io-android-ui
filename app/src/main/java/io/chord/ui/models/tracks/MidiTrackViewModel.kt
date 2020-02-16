package io.chord.ui.models.tracks

import android.widget.SeekBar
import io.chord.clients.models.MidiTrack
import io.chord.clients.models.Track

open class MidiTrackViewModel : TrackViewModel()
{
	var channel: Int = 1

	override fun toModel(): Track
	{
		val model = super.toModel() as MidiTrack
		model.channel = this.channel
		return model
	}

	override fun fromModel(model: Track)
	{
		super.fromModel(model)
		
		if(model !is MidiTrack)
		{
			throw ClassCastException("Expected MidiTrack type as model")
		}
		
		this.fromModel(this.model as MidiTrack)
	}
	
	private fun fromModel(model: MidiTrack)
	{
		this.channel = model.channel
	}

	fun onValueChanged(view: SeekBar, value: Int, fromUser: Boolean)
	{
		this.notifyChange()
	}
}