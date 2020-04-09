package io.chord.ui.behaviors

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface Binder
{
	fun attach(id: Int)
	fun attach(view: View)
	fun attach(fragment: Fragment)
	fun attach(activity: FragmentActivity)
	fun attachAll(views: List<View>)
	fun detach(id: Int)
	fun detach(view: View)
	fun detachAll()
}