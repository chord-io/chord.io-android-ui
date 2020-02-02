package io.chord.ui.behaviors

import android.view.View
import io.chord.ui.components.Binder
import io.chord.ui.extensions.getParentRootView

class BindBehavior<T>(
	private val controller: View
) : Binder
{
	private var controls: MutableMap<Int, T> = mutableMapOf()
	
	// TODO change to nullable type and check if is null
	lateinit var onAttach: ((T) -> Unit)
	lateinit var onDispatchEvent: ((T) -> Unit)
	
	private fun checkIfAlreadyAttached(id: Int)
	{
		if(this.controls.containsKey(id))
		{
			throw IllegalStateException("view is already attached")
		}
	}
	
	override fun attach(id: Int)
	{
		this.checkIfAlreadyAttached(id)
		
		val rootView = this.controller.getParentRootView()
		val control = rootView.findViewById<View>(id)
		this.attach(control)
	}
	
	override fun attach(view: View)
	{
		this.checkIfAlreadyAttached(view.id)
		
		val control: T = view as? T ?: throw TypeCastException("cannot cast to class type")
		
		this.controls[view.id] = control
		this.onAttach(control)
		this.dispatchEvent(control)
	}
	
	override fun attachAll(views: List<View>)
	{
		views.forEach {
			this.attach(it)
		}
	}
	
	override fun detach(id: Int)
	{
		this.controls.remove(id)
	}
	
	override fun detachAll()
	{
		this.controls.clear()
	}
	
	fun requestDispatchEvent()
	{
		this.dispatchEvent()
	}
	
	private fun dispatchEvent()
	{
		this.controls.forEach { (_, control) ->
			this.dispatchEvent(control)
		}
	}
	
	private fun dispatchEvent(control: T)
	{
		this.onDispatchEvent(control)
	}
}