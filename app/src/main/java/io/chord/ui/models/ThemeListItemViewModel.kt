package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Theme

class ThemeListItemViewModel(
	model: Theme
) : BaseObservable()
{
	var name: String = model.name
}