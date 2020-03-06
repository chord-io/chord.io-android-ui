package io.chord.ui.components

import io.chord.ui.behaviors.Bindable

interface Selectable<T> : Bindable
{
	fun setItem(item: T)
}