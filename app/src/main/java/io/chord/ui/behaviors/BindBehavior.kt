package io.chord.ui.behaviors

import android.view.View
import androidx.fragment.app.Fragment
import io.chord.ui.extensions.getParentRootView

class BindBehavior<T: Bindable> (
	private val controller: Binder
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
		val rootView = (this.controller as View).getParentRootView()
		val control = rootView.findViewById<View>(id)
		this.attach(control)
	}
	
	override fun attach(view: View)
	{
		this.checkIfAlreadyAttached(view.id)
		val control: T = view as? T ?: throw TypeCastException("cannot cast to class type")
		this.attach(view.id, control)
	}
	
	override fun attach(fragment: Fragment)
	{
		this.checkIfAlreadyAttached(fragment.id)
		val control: T = fragment as? T ?: throw TypeCastException("cannot cast to class type")
		this.attach(fragment.id, control)
	}
	
	fun attach(control: T)
	{
		val id = (control as View).id
		this.attach(id, control)
	}
	
	fun attach(id: Int, control: T)
	{
		this._controls[id] = control
		this.onAttach(control)
		control.attach(this as BindBehavior<Bindable>)
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
		val control = this._controls.remove(id)!!
		this.detach(control)
	}
	
	fun detach(control: T)
	{
		val keys = this._controls
			.filterValues {
				it == control
			}
			.keys
		
		if(keys.isNotEmpty())
		{
			this._controls.remove(keys.first())
		}
	}
	
	override fun detachAll()
	{
		this._controls.forEach {
			this.detach(it.value)
		}
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