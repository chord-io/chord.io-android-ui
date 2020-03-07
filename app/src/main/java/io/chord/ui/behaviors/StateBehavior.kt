package io.chord.ui.behaviors

import io.chord.ui.components.EditorMode
import io.chord.ui.components.Modulable
import io.chord.ui.gestures.SimpleOnGestureListener

class StateBehavior(
	val gesture: SurfaceGestureBehavior
) : Modulable<EditorMode>
{
	interface State
	
	private var _state: State? = null
	private var _mode: EditorMode =
		EditorMode.None
	val modes: MutableMap<EditorMode, State> = mutableMapOf()
	
	val state: State?
		get() = this._state
	
	val mode: EditorMode
		get() = this._mode
	
	override fun setMode(mode: EditorMode)
	{
		this._mode = mode
		
		if(this.modes.containsKey(mode))
		{
			this._state = this.modes[mode]
			this.gesture.setListener(this._state as SimpleOnGestureListener)
		}
		else
		{
			this.gesture.clearListener()
		}
	}
}