package io.chord.ui.models

import android.app.Application
import android.util.Log
import android.widget.SeekBar
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.chord.BR
import io.chord.client.models.Chord
import io.chord.client.models.Project
import io.chord.client.models.ProjectDto
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.ui.ChordIOApplication
import org.koin.android.ext.android.inject

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