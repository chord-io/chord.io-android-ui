package io.chord.ui.fragments.editor

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import io.chord.ui.activities.EditorActivity
import io.chord.ui.behaviors.FragmentTransation

open class EditorFragmentTransaction<TLane: ViewDataBinding, TRoll: ViewDataBinding>(
	protected val activity: EditorActivity
) : FragmentTransation
{
	protected val laneTag = "lane"
	protected val rollTag = "roll"
	
	lateinit var laneFactory: (() -> Pair<Int, EditorFragment<TLane>>)
	lateinit var rollFactory: (() -> Pair<Int, EditorFragment<TRoll>>)
	lateinit var onLaneLoaded: ((TLane) -> Unit)
	lateinit var onRollLoaded: ((TRoll) -> Unit)
	lateinit var onLaneUnloaded: ((TLane) -> Unit)
	lateinit var onRollUnloaded: ((TRoll) -> Unit)
	
	override fun load(manager: FragmentManager)
	{
		val transaction = manager.beginTransaction()
		val lane = this.laneFactory()
		val roll = this.rollFactory()
		lane.second.onLoaded = this.onLaneLoaded
		roll.second.onLoaded = this.onRollLoaded
		transaction.replace(lane.first, lane.second, this.laneTag)
		transaction.replace(roll.first, roll.second, this.rollTag)
		transaction.commitNow()
	}
	
	override fun unload(manager: FragmentManager)
	{
		val transation = manager.beginTransaction()
		val lane = manager.findFragmentByTag(this.laneTag) as? EditorFragment<TLane>
		val roll = manager.findFragmentByTag(this.rollTag) as? EditorFragment<TRoll>
		if(lane != null && roll != null)
		{
			lane.onUnloaded = this.onLaneUnloaded
			roll.onUnloaded = this.onRollUnloaded
			transation.remove(lane)
			transation.remove(roll)
			transation.commitNow()
		}
	}
}