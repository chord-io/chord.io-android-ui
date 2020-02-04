package io.chord.ui.behaviors

class BindableBehavior(
	private val control: Bindable
) : Bindable
{
	private var _controllers: MutableList<BindBehavior<Bindable>> = mutableListOf()
	
	override fun selfAttach()
	{
		this._controllers.forEach {
			it.attach(this.control)
		}
	}
	
	override fun selfDetach()
	{
		this._controllers.forEach {
			it.detach(this.control)
		}
	}
	
	override fun attach(controller: BindBehavior<Bindable>)
	{
		this._controllers.add(controller)
	}
}