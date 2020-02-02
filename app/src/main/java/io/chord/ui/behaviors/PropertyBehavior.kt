package io.chord.ui.behaviors

class PropertyBehavior<T>(
	private var value: T
)
{
	lateinit var onValueChanged: ((T) -> Unit)
	
	fun getValue(): T
	{
		return this.value
	}
	
	fun setValue(value: T)
	{
		this.value = value
		this.onValueChanged(this.value)
	}
}