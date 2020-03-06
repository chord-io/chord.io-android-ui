package io.chord.ui.components

interface Countable
{
	fun setCounter(counter: () -> List<Int>)
}