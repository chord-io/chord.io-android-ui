package io.chord.ui.models

import androidx.databinding.BaseObservable
import io.chord.clients.models.Theme

open class ThemeViewModel: BaseObservable()
{
	var name: String = ""
	
	protected lateinit var model: Theme
	
	fun toModel(): Theme
	{
		this.model.name = this.name
		return this.model
	}

	fun fromModel(model: Theme) {
		this.name = model.name
		this.model = model
	}
}