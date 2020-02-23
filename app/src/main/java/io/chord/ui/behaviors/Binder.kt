package io.chord.ui.behaviors

import android.view.View
import androidx.fragment.app.Fragment

interface Binder
{
	fun attach(id: Int)
	fun attach(view: View)
	fun attach(fragment: Fragment)
	fun attachAll(views: List<View>)
	fun detach(id: Int)
	fun detachAll()
}