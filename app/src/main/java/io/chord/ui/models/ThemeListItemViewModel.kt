package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.*

class ThemeListItemViewModel(
	private val model: Theme
) : BaseObservable()
{
	var name: String = model.name
	
	var chords: Int = model.chords.size
}