package io.chord.ui.dialogs.flows

import androidx.fragment.app.FragmentActivity
import io.chord.clients.models.MidiTrack
import io.chord.clients.models.Track

class TrackFlow(
	private val context: FragmentActivity
)
{
	val midi: MidiTrackFlow = MidiTrackFlow(this.context)
	
	fun fromModel(model: Track): MidiTrackFlow
	{
		return when(model)
		{
			is MidiTrack -> this.midi
			else -> throw NoWhenBranchMatchedException("any related flow not found") as Throwable
		}
	}
}