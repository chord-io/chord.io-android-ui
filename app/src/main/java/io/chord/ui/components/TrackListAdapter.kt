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
	private val viewHolderFactory: ((View) -> TrackListItemViewHolder)
) : ArrayAdapter<Track>(
	context,
	0,
	mutableListOf()
)
{
	private val _items: MutableList<Track> = mutableListOf()
	private val _holders: MutableList<TrackListItemViewHolder> = mutableListOf()
	private val _recycledHolders: MutableList<TrackListItemViewHolder> = mutableListOf()
	
	lateinit var listener: TrackListClickListener
	
	val items: List<Track>
		get() = this._items.toList()
	
	init
	{
		this.setNotifyOnChange(true)
	}
	
	fun recycleView(view: View)
	{
		val holder = this.getViewHolder(view)
		this.recycleView(holder)
	}
	
	private fun recycleView(holder: TrackListItemViewHolder)
	{
		holder.unbind()
		val parent = holder.view.getDirectParentOfType<ViewGroup>()
		parent!!.removeView(holder.view)
		this._holders.remove(holder)
		this._recycledHolders.add(holder)
	}
	
	fun recycleViews()
	{
		this._holders.map {
			it.view
		}
		.forEach {
			this.recycleView(it)
		}
	}
	
	override fun add(item: Track)
	{
		this._items.add(item)
		super.add(item)
	}
	
	override fun addAll(vararg items: Track)
	{
		this._items.addAll(items.toList())
		super.addAll(*items)
	}
	
	override fun addAll(collection: MutableCollection<out Track>)
	{
		this._items.addAll(collection)
		super.addAll(collection)
	}
	
	override fun insert(
		item: Track,
		index: Int
	)
	{
		this._items.add(index, item)
		super.insert(item, index)
	}
	
	fun update(item: Track)
	{
		val holder = this.getViewHolder(item)
		holder.model = item
	}
	
	override fun remove(item: Track)
	{
		val holder = this.getViewHolder(item)
		this.recycleView(holder)
		this._items.remove(item)
		super.remove(item)
	}
	
	override fun clear()
	{
		this._items.clear()
		super.clear()
	}
	
	// TODO Generify
	// TODO recycleviews
	// TODO make this class generic
	
	private fun getViewHolder(item: Track): TrackListItemViewHolder
	{
		val result = this._holders
			.stream()
			.filter {
				it.model == item
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
	): TrackListItemViewHolder
	{
		val result = this._holders
			.stream()
			.filter {
				it.view == view
			}
			.findFirst()
		
		if(result.isPresent)
		{
			return result.get()
		}
		
		return this.viewHolderFactory(view)
	}
	
	fun bindViewHolder(holder: TrackListItemViewHolder, position: Int)
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
		if(this._recycledHolders.size > 0)
		{
			val holder = this._recycledHolders.removeAt(0)
			this.bindViewHolder(holder, position)
			this._holders.add(holder)
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
			this._holders.add(holder)
			return view
		}
	}
}