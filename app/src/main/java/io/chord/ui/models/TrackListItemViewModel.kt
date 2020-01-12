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
import io.chord.client.models.Track
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.ui.ChordIOApplication
import org.koin.android.ext.android.inject

class TrackListItemViewModel(
	private val model: Track
) : BaseObservable()
{
	var name: String = model.name
	
	var channel: Int = model.channel
}