package io.chord.ui.models

import android.widget.SeekBar
import androidx.databinding.BaseObservable
import io.chord.clients.models.Project
import io.chord.clients.models.ProjectDto

class ProjectDialogFormViewModel(
	private val model: Project?
) : BaseObservable()
{
	var name: String = model?.name ?: ""
	
	var tempo: Int = model?.tempo ?: 120
	
	var private: Boolean = model?.isPrivate ?: false
	
	fun toDto(): ProjectDto
	{
		return ProjectDto(
			this.name,
			this.tempo,
			this.private,
			model?.tracks ?: listOf()
		)
	}
	
	fun onValueChanged(view: SeekBar, value: Int, fromUser: Boolean)
	{
		this.notifyChange()
	}
}