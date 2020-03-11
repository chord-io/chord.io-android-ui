package io.chord.ui.extensions

fun <T> MutableList<T>.addIfNotPresent(element: T)
{
	if(!this.contains(element))
	{
		this.add(element)
	}
}