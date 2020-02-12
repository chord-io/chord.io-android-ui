package io.chord.ui.models.tracks

import android.widget.SeekBar
import io.chord.clients.models.MidiTrack

open class MidiTrackViewModel : TrackViewModel()
{
	var channel: Int = 1

	override fun toModel(): MidiTrack
	{
		return MidiTrack(
			this.name,
			this.color,
			this.channel
		)
	}

	fun fromModel(model: MidiTrack)
	{
		this.channel = model.channel
		super.fromModel(model)
	}

	fun onValueChanged(view: SeekBar, value: Int, fromUser: Boolean)
	{
		this.notifyChange()
	}
}