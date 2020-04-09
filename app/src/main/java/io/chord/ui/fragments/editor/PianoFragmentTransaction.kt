package io.chord.ui.fragments.editor

import io.chord.R
import io.chord.databinding.PianoLaneFragmentBinding
import io.chord.databinding.PianoRollFragmentBinding
import io.chord.ui.activities.EditorActivity

class PianoFragmentTransaction(
	activity: EditorActivity
) : EditorFragmentTransaction<PianoLaneFragmentBinding, PianoRollFragmentBinding>(activity)
{
	init
	{
		this.laneFactory = {
			R.id.laneScrollview to PianoLaneFragment(this.activity.theme)
		}
		this.rollFactory = {
			R.id.rollScrollview to PianoRollFragment(this.activity.theme)
		}
		this.onLaneLoaded = { binding ->
			this.activity.binding.verticalZoomBar.attach(binding.lane)
		}
		this.onRollLoaded = { binding ->
			binding.roll.setCounter(this.activity::counter)
			this.activity.binding.verticalZoomBar.attach(binding.roll)
			this.activity.binding.horizontalZoomBar.attach(binding.roll)
			this.activity.binding.toolbarEditor.quantize.attach(binding.roll)
		}
		this.onLaneUnloaded = { binding ->
			this.activity.binding.verticalZoomBar.detach(binding.lane)
		}
		this.onRollUnloaded = { binding ->
			this.activity.binding.verticalZoomBar.detach(binding.roll)
			this.activity.binding.horizontalZoomBar.detach(binding.roll)
			this.activity.binding.toolbarEditor.quantize.detach(binding.roll)
		}
	}
}