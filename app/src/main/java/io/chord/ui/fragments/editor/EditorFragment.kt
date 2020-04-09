package io.chord.ui.fragments.editor

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.chord.clients.models.Theme

open class EditorFragment<T: ViewDataBinding>(
	private val theme: Theme
) : Fragment()
{
	var onLoaded: ((T) -> Unit)? = null
	var onUnloaded: ((T) -> Unit)? = null
	
	protected lateinit var binding: T
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)
		this.onLoaded?.invoke(this.binding)
	}
	
	override fun onDestroyView()
	{
		super.onDestroyView()
		this.onUnloaded?.invoke(this.binding)
	}
}