package io.chord.ui.extensions

import io.chord.clients.models.DrumTrack
import io.chord.clients.models.MidiTrack
import io.chord.clients.models.Track
import io.chord.ui.models.tracks.DrumTrackViewModel
import io.chord.ui.models.tracks.MidiTrackViewModel
import io.chord.ui.models.tracks.TrackViewModel

fun Track.getViewModel(): TrackViewModel
{
    return when(this)
    {
        is MidiTrack -> {
            val model = MidiTrackViewModel()
            model.fromModel(this)
            return model
        }
        is DrumTrack -> {
            val model = DrumTrackViewModel()
            model.fromModel(this)
            return model
        }
        else -> MidiTrackViewModel()
    }
}