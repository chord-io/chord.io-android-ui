package io.chord.ui.models

import android.widget.SeekBar
import androidx.databinding.BaseObservable
import io.chord.clients.models.Project
import io.chord.clients.models.Visibility

class ProjectViewModel(
) : BaseObservable()
{
	var name: String = ""
	
	var tempo: Int = 120
	
	var visibility: Visibility = Visibility.Private
	
	private lateinit var model: Project
	
	fun toModel(): Project
	{
		return Project(
			this.model.id,
			this.name,
			this.tempo,
			this.visibility,
			this.model.tracks,
			this.model.themes
		)
	}
	
	fun fromModel(model: Project)
	{
		this.model = model
		this.name = model.name
		this.tempo = model.tempo
		this.visibility = model.visibility
	}
	
	fun onValueChanged(view: SeekBar, value: Int, fromUser: Boolean)
	{
		this.notifyChange()
	}

	fun onVisibilityChanged(visibility: Boolean)
	{
		this.visibility = if(visibility) Visibility.Public else Visibility.Private
	}
}