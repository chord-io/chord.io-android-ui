package io.chord.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.chord.R
import io.chord.ui.components.ScrollBar
import io.chord.ui.components.TwoDimensionalScrollView

class SequencerActivity : AppCompatActivity()
{
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.setContentView(R.layout.activity_sequencer)
		this.setSupportActionBar(this.findViewById(R.id.toolbar))
		
		val horizontalScrollBar = this.findViewById<ScrollBar>(R.id.horizontalScrollBar)
		val verticalScrollBar = this.findViewById<ScrollBar>(R.id.verticalScrollBar)
		
		horizontalScrollBar.attachScrollView(R.id.scrollview)
		horizontalScrollBar.attachScrollView(R.id.scrollview2)
		verticalScrollBar.attachScrollView(R.id.scrollview)
	}
}
