package io.chord.ui.fragments.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.clients.models.Theme
import io.chord.databinding.PianoLaneFragmentBinding

class PianoLaneFragment(
	theme: Theme
) : EditorFragment<PianoLaneFragmentBinding>(theme)
{
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		super.onCreateView(inflater, container, savedInstanceState)
		
		val view = LayoutInflater
			.from(this.context)
			.inflate(
				R.layout.piano_lane_fragment,
				container,
				false
			)
		
		this.binding = DataBindingUtil.bind(view)!!
		
		return view
	}
}