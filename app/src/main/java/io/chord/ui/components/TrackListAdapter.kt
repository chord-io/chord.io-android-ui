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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
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
	private val views: MutableList<TrackListItem> = mutableListOf()
	private val recycledViews: MutableList<TrackListItem> = mutableListOf()
	
	init
	{
		this.setNotifyOnChange(true)
	}
	
	// TODO recycleviews
	// TODO make this class generic
	
	fun getViewHolder(
		view: View
	): TrackListItem
	{
		val result = this.views
			.stream()
			.filter {
				it.view == view
			}
			.findFirst()
		
		if(result.isPresent)
		{
			return result.get()
		}
		
		return TrackListItem(view)
	}
	
	fun bindViewHolder(holder: TrackListItem, position: Int)
	{
		holder.bind(TrackListItemViewModel(this.getItem(position)!!))
	}
	
	@SuppressLint("ViewHolder")
	override fun getView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		val view = LayoutInflater
			.from(this.context)
			.inflate(
				R.layout.track_list_item,
				parent,
				false
			)
		val holder = this.getViewHolder(view)
		this.bindViewHolder(holder, position)
		this.views.add(holder)
		return view
	}
}