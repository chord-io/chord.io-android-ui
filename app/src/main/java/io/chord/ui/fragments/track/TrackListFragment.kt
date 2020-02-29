package io.chord.ui.fragments.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.chord.R
import io.chord.clients.models.MidiTrack
import io.chord.clients.models.Track
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.Binder
import io.chord.ui.components.ListClickListener
import io.chord.ui.components.TrackControl
import io.chord.ui.components.TrackList
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.SelectCudcOperationDialog
import io.chord.ui.dialogs.customs.SelectTrackTypeDialog
import io.chord.ui.dialogs.flows.TrackFlow
import io.chord.ui.extensions.getRootView
import java.util.*

class TrackListFragment : Fragment(), ListClickListener<Track>, Binder
{
	private lateinit var flow: TrackFlow
	private lateinit var list: TrackList
	private lateinit var controlMaster: TrackControl
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		this.flow = TrackFlow(this.activity!!)
		
		this.list = LayoutInflater
			.from(this.context)
			.inflate(
				R.layout.track_list_fragment,
				container,
				false
			) as TrackList
		
		return this.list
	}
	
	override fun onActivityCreated(savedInstanceState: Bundle?)
	{
		super.onActivityCreated(savedInstanceState)
		this.list.listener = this
		this.list.trackControlMaster  = this.activity!!.getRootView()
			.findViewById(R.id.trackControlMaster)
		this.loadTracks()
	}
	
	override fun onItemClicked(item: Track)
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
	}
	
	override fun onDragEnded(from: Int, to: Int)
	{
		ProjectManager.tracks.move(from, to)
		// TODO: handle observable
		ProjectManager.update()
	}
	
	fun create()
	{
		val dialog = SelectTrackTypeDialog(this.activity!!)
		
		dialog.onValidate = {}
		dialog.onMidiSelected = {
			this.flow.midi.create()
		}
		
		dialog.show()
	}
	
	private fun update(item: Track)
	{
		this.flow.fromModel(item).update(item as MidiTrack)
	}
	
	private fun delete(item: Track)
	{
		this.flow.fromModel(item).delete(item as MidiTrack)
	}
	
	private fun clone(item: Track)
	{
		this.flow.fromModel(item).clone(item as MidiTrack)
	}
	
	private fun loadTracks()
	{
		val tracks = ProjectManager.getCurrent()!!.tracks
		this.list.addAll(tracks)
	}
	
	override fun attach(id: Int)
	{
		this.list.attach(id)
	}
	
	override fun attach(view: View)
	{
		this.list.attach(view)
	}
	
	override fun attach(fragment: Fragment)
	{
		this.list.attach(fragment)
	}
	
	override fun attachAll(views: List<View>)
	{
		this.list.attachAll(views)
	}
	
	override fun detach(id: Int)
	{
		this.detach(id)
	}
	
	override fun detachAll()
	{
		this.detachAll()
	}
}