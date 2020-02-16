package io.chord.ui.fragments.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.MidiTrack
import io.chord.clients.observe
import io.chord.services.managers.ProjectManager
import io.chord.ui.components.TrackControl
import io.chord.ui.components.TrackList
import io.chord.ui.components.TrackListClickListener
import io.chord.ui.components.TrackListItemViewHolder
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.SelectCudcOperationDialog
import io.chord.ui.dialogs.customs.SelectTrackTypeDialog
import io.chord.ui.dialogs.flows.TrackFlow
import io.chord.ui.extensions.getRootView
import java.util.*

class TrackListFragment : Fragment(), TrackListClickListener
{
	private lateinit var flow: TrackFlow
	private lateinit var trackList: TrackList
	private lateinit var trackControlMaster: TrackControl
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		this.flow = TrackFlow(this.activity!!)
		
		this.trackList = LayoutInflater
			.from(this.context)
			.inflate(
				R.layout.track_list_fragment,
				container,
				false
			) as TrackList
		
		return this.trackList
	}
	
	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		this.trackList.listener = this
		this.trackList.trackControlMaster  = this.activity!!.getRootView()
			.findViewById(R.id.trackControlMaster)
		this.loadTracks()
	}
	
	override fun onItemClicked(item: TrackListItemViewHolder)
	{
	
	}
	
	override fun onItemLongClicked(item: TrackListItemViewHolder): Boolean
	{
		val dialog = SelectCudcOperationDialog(
			this.activity!!,
			EnumSet.of(
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
	
	fun create()
	{
		val dialog = SelectTrackTypeDialog(this.activity!!)
		
		dialog.onValidate = {}
		dialog.onMidiSelected = {
			this.flow.midi.create()
				.doOnSuccess {
					this.trackList.add(it)
				}
				.observe()
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
			.observe()
	}
	
	private fun delete(item: TrackListItemViewHolder)
	{
		val model = item.model
		this.flow.fromModel(model).delete(model as MidiTrack)
			.doOnSuccess {
				this.trackList.remove(model)
			}
			.observe()
	}
	
	private fun clone(item: TrackListItemViewHolder)
	{
		val model = item.model
		this.flow.fromModel(model).clone(model as MidiTrack)
			.doOnSuccess {
				this.trackList.add(it)
			}
			.observe()
	}
	
	private fun loadTracks()
	{
		val tracks = ProjectManager.getCurrent()!!.tracks
		this.trackList.addAll(tracks)
	}
}