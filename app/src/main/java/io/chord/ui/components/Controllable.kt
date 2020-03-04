package io.chord.ui.components

interface Controllable<T>
{
	fun setControlState(state: T)
}