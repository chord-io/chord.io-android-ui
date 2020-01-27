package io.chord.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.clients.models.Track
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.models.TrackListItemViewModel
import io.chord.ui.utils.ViewUtils

class TrackListAdapter(
	context: Context
) : ArrayAdapter<Track>(
	context,
	0,
	mutableListOf()
)
{
	init
	{
		this.setNotifyOnChange(true)
	}
	
	@SuppressLint("ViewHolder")
	override fun getView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		val view = LayoutInflater.from(this.context).inflate(
			R.layout.track_list_item,
			parent,
			false
		)
		val track = this.getItem(position)
		val binding = DataBindingUtil.bind<TrackListItemBinding>(view)!!
		binding.track = TrackListItemViewModel(track)
		return view
	}
}