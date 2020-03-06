package io.chord.ui.components

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.chord.clients.models.Track

class TrackDropDownAdapter(
	context: Context,
	resource: Int,
	tracks: List<Track>
) : ArrayAdapter<Track>(
	context,
	resource,
	tracks
)
{
	fun getText(position: Int): String
	{
		val item = this.getItem(position)!!
		return item.name
	}
	
	override fun getView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		val view = super.getView(position, convertView, parent) as TextView
		view.text = this.getText(position)
		return view
	}
	
	override fun getDropDownView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		val view = super.getDropDownView(position, convertView, parent) as TextView
		view.text = this.getText(position)
		return view
	}
}