package io.chord.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.chord.R
import io.chord.clients.models.Track
import io.chord.ui.extensions.getDirectParentOfType
import io.chord.ui.models.TrackListItemViewModel

class TrackListAdapter(
	context: Context,
	private val listener: TrackListClickListener
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
	
	fun recycleView(view: View)
	{
		val holder = this.getViewHolder(view)
		this.recycleView(holder)
	}
	
	private fun recycleView(holder: TrackListItem)
	{
		val parent = holder.view.getDirectParentOfType<ViewGroup>()
		parent!!.removeView(holder.view)
		this.views.remove(holder)
		this.recycledViews.add(holder)
	}
	
	fun recycleViews()
	{
		this.views.map {
			it.view
		}
		.forEach {
			this.recycleView(it)
		}
	}
	
	override fun remove(item: Track)
	{
		val holder = this.getViewHolder(item)
		holder.unbind()
		this.recycleView(holder)
		super.remove(item)
	}
	
	fun update(item: Track)
	{
		val holder = this.getViewHolder(item)
		holder.binding.track!!.model = item
		holder.binding.notifyChange()
	}
	
	// TODO Generify
	// TODO recycleviews
	// TODO make this class generic
	
	private fun getViewHolder(item: Track): TrackListItem
	{
		val result = this.views
			.stream()
			.filter {
				it.binding.track!!.model == item
			}
			.findFirst()
		
		if(!result.isPresent)
		{
			throw IllegalStateException("item does not exist")
		}
		
		return result.get()
	}
	
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
		holder.bind(TrackListItemViewModel(this.getItem(position)!!), this.listener)
	}
	
	@SuppressLint("ViewHolder")
	override fun getView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		if(this.recycledViews.size > 0)
		{
			val holder = this.recycledViews.removeAt(0)
			this.bindViewHolder(holder, position)
			this.views.add(holder)
			return holder.view
		}
		else
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
}