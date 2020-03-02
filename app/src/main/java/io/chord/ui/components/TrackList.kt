package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import io.chord.R
import io.chord.clients.models.Track
import io.chord.services.managers.ProjectManager
import io.chord.ui.fragments.track.TrackListItemViewHolder
import io.chord.ui.models.tracks.TrackListItemViewModel

class TrackList : ListView<Track, TrackListItemViewModel, TrackListItemViewHolder>
{
	private companion object
	{
		const val layoutId = R.layout.track_list_item
	}
	
	private lateinit var _trackControlMaster: TrackControl
	
	var trackControlMaster: TrackControl
		get() = this._trackControlMaster
		set(value) {
			this._trackControlMaster = value
			this.populateLayout()
		}
	
	constructor(context: Context?) : super(context)
	{
		this.init(null, 0)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?
	) : super(context, attrs)
	{
		this.init(attrs, 0)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.init(attrs, defStyleAttr)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.init(attrs, defStyleAttr)
	}
	
	override fun init(attrs: AttributeSet?, defStyle: Int)
	{
		super.init(attrs, defStyle)
		
		this.adapter.layoutId = layoutId
		
		this.adapter.viewHolderFactory = {
			TrackListItemViewHolder(it)
		}
		
		this.adapter.viewModelFactory = {
			TrackListItemViewModel(it)
		}
		
		ProjectManager.addOnUpdateListener(this::update)
		ProjectManager.addOnDeleteListener(this::update)
	}
	
	override fun onViewHolder(holder: TrackListItemViewHolder)
	{
		holder.trackControlMaster = this.trackControlMaster
	}
	
	private fun update()
	{
		val tracks = ProjectManager.getCurrent()!!.tracks.toMutableList()
		val notifyOnChange = this.adapter.getNotifyOnChange()
		this.adapter.setNotifyOnChange(false)
		this.adapter.clear()
		this.adapter.addAll(tracks)
		this.adapter.notifyDataSetChanged()
		this.requestDispatchEvent()
		this.adapter.setNotifyOnChange(notifyOnChange)
	}
}