package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Project
import io.chord.clients.models.Visibility

class ProjectListItemViewModel(
	model: Project
) : BaseObservable()
{
	var name: String = model.name
	
	var tempo: Int = model.tempo
	
	var visibility: Visibility = model.visibility
	
	var tracks: Int = model.tracks.size
}