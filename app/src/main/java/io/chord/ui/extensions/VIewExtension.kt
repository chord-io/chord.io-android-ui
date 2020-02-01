package io.chord.ui.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.FragmentActivity

fun View.getChildrens(): List<View>
{
	var depth = 0
	val views: MutableMap<Int, MutableList<View>> = mutableMapOf()
	views[depth] = mutableListOf(this)
	
	while(true)
	{
		val currentViews = views[depth]
		val nextViews = mutableListOf<View>()
		
		currentViews!!.forEach { view ->
			if(view is ViewGroup)
			{
				view.forEach { children ->
					nextViews.add(children)
				}
			}
		}
		
		if(nextViews.size == 0)
		{
			break
		}
		
		depth++
		
		views[depth] = nextViews
	}
	
	return views.flatMap {
		it.value
	}
}

fun View.setViewState(state: Boolean)
{
	this.getChildrens().forEach {
		it.isEnabled = state
	}
}

fun View.getDirectChildrens(): List<View>?
{
	if(this is ViewGroup)
	{
		val list = mutableListOf<View>()
		this.forEach { children ->
			list.add(children)
		}
		return list
	}
	
	return null
}

fun View.getDirectChildren(): View?
{
	if(this is ViewGroup)
	{
		return this.getChildAt(0)
	}
	
	return null
}

inline fun <reified TType: View> View.getChildOfType(): List<TType>
{
	return this.getChildrens()
		.filterIsInstance<TType>()
}

fun FragmentActivity.getRootView(): View
{
	val view = this.findViewById<ViewGroup>(android.R.id.content)
	return view.getChildAt(0)
}

fun View.getParentRootView(): View
{
	var lastParent = this.parent
	
	while(true)
	{
		val parent = lastParent.parent
		
		if(parent != null && parent is View && parent is ViewGroup)
		{
			lastParent = parent
		}
		else
		{
			break
		}
	}
	
	return lastParent as View
}