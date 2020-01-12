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
import io.chord.client.models.*
import io.chord.services.authentication.storage.SharedPreferencesAuthenticationStorage
import io.chord.ui.ChordIOApplication
import org.koin.android.ext.android.inject

class ThemeListItemViewModel(
	private val model: Theme
) : BaseObservable()
{
	var name: String = model.name
	
	var chords: Int = model.chords.size
}