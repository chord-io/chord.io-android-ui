package io.chord.ui.behaviors

interface Bindable
{
	fun attach(controller: BindBehavior<Bindable>)
	fun selfAttach()
	fun selfDetach()
}