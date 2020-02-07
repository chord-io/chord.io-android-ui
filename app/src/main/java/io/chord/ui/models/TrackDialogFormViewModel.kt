package io.chord.ui.models

import android.widget.SeekBar
import androidx.databinding.BaseObservable
import io.chord.clients.models.Track

class TrackDialogFormViewModel(
	val model: Track?
) : BaseObservable()
{
	var name: String = this.model?.name ?: ""
	
	var channel: Int = this.model?.channel ?: 1
	
	fun onValueChanged(view: SeekBar, value: Int, fromUser: Boolean)
	{
		this.notifyChange()
	}
	
	fun toModel(): Track
	{
		this.model!!.name = this.name
		this.model.channel = this.channel
		return this.model
	}
	
	fun fromModel(model: Track)
	{
		this.name = model.name
		this.channel = model.channel
	}
}