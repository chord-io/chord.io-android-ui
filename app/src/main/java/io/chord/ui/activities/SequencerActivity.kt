package io.chord.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.chord.R
import io.chord.clients.models.Track
import io.chord.ui.components.*
import kotlinx.android.synthetic.main.activity_sequencer.*

class SequencerActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_sequencer)
		this.setSupportActionBar(this.findViewById(R.id.toolbar))
		
		val horizontalScrollBar = this.findViewById<ScrollBar>(R.id.horizontalScrollBar)
		horizontalScrollBar.attach(R.id.scrollview)
		val verticalScrollBar = this.findViewById<ScrollBar>(R.id.verticalScrollBar)
//		verticalScrollBar.attach(R.id.scrollview2)
		verticalScrollBar.attach(R.id.trackListScrollView)
		
		val horizontalZoomBar = this.findViewById<ZoomBar>(R.id.horizontalZoomBar)
		horizontalZoomBar.attach(R.id.ruler)
		
		val veticalZoomBar = this.findViewById<ZoomBar>(R.id.verticalZoomBar)
		verticalZoomBar.attach(R.id.trackList)
		
		this.findViewById<QuantizeDropDown>(R.id.quantizeDropDown)
			.attach(R.id.ruler)
		
		val trackControlMaster = this.findViewById<TrackControl>(R.id.trackControlMaster)
		val trackList = this.findViewById<TrackList>(R.id.trackList)
		trackList.trackControlMaster = trackControlMaster
		
		trackList
			.add(Track("track title too long", 10, mutableListOf()))
		
		trackList
			.add(Track("test2", 10, mutableListOf()))
		
		trackList
			.add(Track("test2", 10, mutableListOf()))
		
		trackList
			.add(Track("test2", 10, mutableListOf()))
		
		trackList
			.add(Track("test2", 10, mutableListOf()))
		
		trackList
			.add(Track("test2", 10, mutableListOf()))
		
		trackList
			.add(Track("test2", 10, mutableListOf()))
	}
}
