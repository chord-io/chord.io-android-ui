package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Project

class ProjectListItemViewModel(
	private val model: Project
) : BaseObservable()
{
	var name: String = model.name
	
	var tempo: Int = model.tempo
	
	var private: Boolean = model.isPrivate
	
	var tracks: Int = model.tracks.size
}