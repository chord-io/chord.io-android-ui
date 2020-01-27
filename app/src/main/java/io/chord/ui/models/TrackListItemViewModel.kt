package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Project
import io.chord.clients.models.Track

class TrackListItemViewModel(
	private val model: Track
) : BaseObservable()
{
	var name: String = model.name
	
	var channel: Int = model.channel
}