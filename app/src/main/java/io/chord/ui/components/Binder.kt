package io.chord.ui.components

interface Binder
{
	fun attach(id: Int)
	fun detach(id: Int)
	// TODO: addAll, removeAll
}