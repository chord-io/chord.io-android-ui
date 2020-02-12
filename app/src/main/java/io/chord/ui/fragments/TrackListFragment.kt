package io.chord.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.MidiTrack
import io.chord.ui.components.TrackControl
import io.chord.ui.components.TrackList
import io.chord.ui.components.TrackListClickListener
import io.chord.ui.components.TrackListItemViewHolder
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.SelectCudcOperationDialog
import io.chord.ui.dialogs.customs.SelectTrackTypeDialog
import io.chord.ui.dialogs.flows.TrackFlow
import java.util.*

class TrackListFragment : Fragment(), TrackListClickListener
{
	private val flow: TrackFlow = TrackFlow(this.activity!!)
	private lateinit var trackList: TrackList
	private lateinit var trackControlMaster: TrackControl
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		val trackControlMaster = this.activity!!.findViewById<TrackControl>(R.id.trackControlMaster)
		
		this.trackList = LayoutInflater
			.from(this.context)
			.inflate(
				R.layout.track_list_fragment,
				container,
				false
			) as TrackList
		
		this.trackList.listener = this
		this.trackList.trackControlMaster = trackControlMaster
		
		return this.trackList
	}
	
	override fun onItemClicked(item: TrackListItemViewHolder)
	{
	
	}
	
	override fun onItemLongClicked(item: TrackListItemViewHolder): Boolean
	{
		val dialog = SelectCudcOperationDialog(
			this.activity!!,
			EnumSet.of(
				CudcOperation.CREATE,
				CudcOperation.UPDATE,
				CudcOperation.DELETE,
				CudcOperation.CLONE
			)
		)
		
		dialog.onValidate = {}
		dialog.onCreateSelected = { this.create() }
		dialog.onDeleteSelected = { this.delete(item) }
		dialog.onUpdateSelected = { this.update(item) }
		dialog.onCloneSelected = { this.clone(item) }
		
		dialog.show()
		
		return true
	}
	
	private fun create()
	{
		val dialog = SelectTrackTypeDialog(this.activity!!)
		
		dialog.onValidate = {}
		dialog.onMidiSelected = {
			this.flow.midi.create()
				.doOnSuccess {
					this.trackList.add(it)
				}
		}
		
		dialog.show()
	}
	
	private fun update(item: TrackListItemViewHolder)
	{
		val model = item.model
		this.flow.fromModel(model).update(model as MidiTrack)
			.doOnSuccess {
				this.trackList.update(it)
			}
	}
	
	private fun delete(item: TrackListItemViewHolder)
	{
		val model = item.model
		this.flow.fromModel(model).delete(model as MidiTrack)
			.doOnSuccess {
				this.trackList.remove(model)
			}
	}
	
	private fun clone(item: TrackListItemViewHolder)
	{
		val model = item.model
		this.flow.fromModel(model).clone(model as MidiTrack)
			.doOnSuccess {
				this.trackList.add(it)
			}
	}
}