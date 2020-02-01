package io.chord.ui.components

import android.view.View

interface Binder
{
	fun attach(id: Int)
	fun attach(view: View)
	fun attachAll(views: List<View>)
	fun detach(id: Int)
	fun detachAll()
}