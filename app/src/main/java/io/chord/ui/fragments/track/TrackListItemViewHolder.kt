package io.chord.ui.fragments.track

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.clients.models.Track
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.components.ListClickListener
import io.chord.ui.components.ListViewHolder
import io.chord.ui.components.TrackControl
import io.chord.ui.components.TrackControlState
import io.chord.ui.extensions.getChildOfType
import io.chord.ui.extensions.toHexaDecimalString
import io.chord.ui.extensions.toTransparent
import io.chord.ui.models.tracks.TrackListItemViewModel

class TrackListItemViewHolder(
	override val view: View
) : ListViewHolder<Track, TrackListItemViewModel>
{
	private companion object
	{
		val holders: MutableList<TrackListItemViewHolder> = mutableListOf()
		val states: MutableMap<String, TrackControlState> = mutableMapOf()
	}
	
	private lateinit var control: TrackControl
	private var _trackControlMaster: TrackControl? = null
	private val binding: TrackListItemBinding = DataBindingUtil.bind(this.view)!!
	
	override var model: Track
		get() = this.binding.track!!.model
		set(value) {
			this.binding.track!!.fromModel(value)
			this.binding.invalidateAll()
			this.setColor(value.color)
		}
	
	var trackControlMaster: TrackControl
		get() = this._trackControlMaster!!
		set(value) {
			if(this._trackControlMaster != value)
			{
				this._trackControlMaster = value
				this._trackControlMaster!!.attach(this.control)
			}
		}
	
	private fun getControl(): TrackControl
	{
		return this.binding.layout.getChildOfType<TrackControl>().first()
	}
	
	private fun setControlState()
	{
		this.control.animate = false
		this.control.state = this.getStateValue()
		this.control.animate = true
	}
	
	private fun getStateKey(): String
	{
		return this.binding.track!!.model.referenceId.toHexaDecimalString()
	}
	
	private fun getStateValue(): TrackControlState
	{
		val tracks = this.binding.track!!.tracks
		states.filterKeys {key ->
			!tracks.any {
				val referenceId = it.referenceId.toHexaDecimalString()
				key == referenceId
			}
		}
		.forEach {
			states.remove(it.key)
		}
		
		val key = this.getStateKey()
		return if(states.containsKey(key))
		{
			states[key]!!
		}
		else
		{
			states[key] = this._trackControlMaster?.state ?: TrackControlState.Normal
			states[key]!!
		}
	}
	
	override fun bind(model: TrackListItemViewModel, listener: ListClickListener<Track>)
	{
		this.binding.track = model
		
		this.control = this.getControl()
		this.setControlState()
		this.control.onStateChanged = {
			states[this.getStateKey()] = it
		}
		
		this.binding.layout.setOnClickListener {
			listener.onItemClicked(this.model)
		}
		
		this.setColor(model.model.color)
	}
	
	// TODO remove this
	override fun unbind(){}
	
	private fun setColor(color: Int)
	{
		this.binding.layout.setBackgroundColor(color.toTransparent(0.1f))
		this.binding.color.setBackgroundColor(color)
	}
}