package io.chord.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.databinding.ActivitySequencerBinding
import io.chord.ui.fragments.track.TrackListFragment

class SequencerActivity : AppCompatActivity()
{
	private lateinit var binding: ActivitySequencerBinding
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		
		val view = LayoutInflater
			.from(this)
			.inflate(
				R.layout.activity_sequencer,
				null,
				false
			)
		
		this.binding = DataBindingUtil.bind(view)!!
		this.binding.toolbarSequencer.editor.toolbar
		this.binding.horizontalScrollBar.attach(R.id.scrollview)
		this.binding.verticalScrollBar.attach(R.id.trackListScrollView)
		this.binding.horizontalZoomBar.attach(R.id.ruler)
		this.binding.verticalZoomBar.attach(R.id.trackList)
		this.binding.toolbarSequencer.quantize.attach(R.id.ruler)
		this.setSupportActionBar(this.binding.toolbarSequencer.editor.toolbar)
		this.setContentView(view)
		
		this.binding.addTrackButton.setOnClickListener {
			val trackList = this.supportFragmentManager.findFragmentById(R.id.trackList) as TrackListFragment
			trackList.create()
		}
	}
}
