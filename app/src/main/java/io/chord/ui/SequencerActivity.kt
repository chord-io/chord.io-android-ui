package io.chord.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.chord.R

class SequencerActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_sequencer)
		this.setSupportActionBar(this.findViewById(R.id.toolbar))
	}
}
