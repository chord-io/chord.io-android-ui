package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Theme

class ThemeListItemViewModel(
	var isPlaying: Boolean
) : BaseObservable()
{
	var name: String = ""
	var sequences: Int = 0
	
	lateinit var model: Theme
	
	fun fromModel(model: Theme)
	{
		this.name = model.name
		this.sequences = model.sequences.size
		this.model = model
		this.notifyChange()
	}
}