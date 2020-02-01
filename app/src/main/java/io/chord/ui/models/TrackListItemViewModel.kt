package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Track

class TrackListItemViewModel(
	model: Track
) : BaseObservable()
{
	var name: String = model.name
}