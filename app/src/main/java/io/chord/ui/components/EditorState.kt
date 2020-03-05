package io.chord.ui.components

import io.chord.ui.gestures.SimpleOnGestureListener

class EditorState(
	val gesture: EditorGesture
) : Modulable<EditorMode>
{
	interface State
	
	private var _state: State? = null
	private var _mode: EditorMode = EditorMode.None
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