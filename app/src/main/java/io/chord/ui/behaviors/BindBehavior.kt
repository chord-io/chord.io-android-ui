package io.chord.ui.behaviors

import android.view.View
import io.chord.ui.components.Binder
import io.chord.ui.extensions.getParentRootView

class BindBehavior<T>(
	private val controller: View
) : Binder
{
	private var _controls: MutableMap<Int, T> = mutableMapOf()
	
	// TODO change to nullable type and check if is null
	lateinit var onAttach: ((T) -> Unit)
	lateinit var onDispatchEvent: ((T) -> Unit)
	
	val controls: MutableList<T>
		get() = this._controls.values.toMutableList()
	
	private fun checkIfAlreadyAttached(id: Int)
	{
		if(this._controls.containsKey(id))
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
		this.attach(view.id, control)
	}
	
	fun attach(id: Int, control: T)
	{
		this._controls[id] = control
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
		this._controls.remove(id)
	}
	
	override fun detachAll()
	{
		this._controls.clear()
	}
	
	fun requestDispatchEvent()
	{
		this.dispatchEvent()
	}
	
	private fun dispatchEvent()
	{
		this._controls.forEach { (_, control) ->
			this.dispatchEvent(control)
		}
	}
	
	private fun dispatchEvent(control: T)
	{
		this.onDispatchEvent(control)
	}
}