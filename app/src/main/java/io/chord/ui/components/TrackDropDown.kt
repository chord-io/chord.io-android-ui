package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.clients.models.Track
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Binder
import kotlinx.android.synthetic.main.component_dropdown.view.*

class TrackDropDown : DropDown, AdapterView.OnItemSelectedListener, Binder
{
	private lateinit var adapter: TrackDropDownAdapter
	private val bindBehavior = BindBehavior<Selectable<Track>>(this)
	private lateinit var selectedTrack: Track
	private val tracks = ProjectManager.getCurrent()!!.tracks
	
	constructor(context: Context?) : super(context)
	
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
	
	override fun init(attrs: AttributeSet?, defStyle: Int) {
		super.init(attrs, defStyle)
		
		this.selectedTrack = this.tracks.first()
		this.adapter = TrackDropDownAdapter(this.context, R.layout.track_dropdown_item, this.tracks)
		this.dropdownView.onItemSelectedListener = this
		this.dropdownView.adapter = this.adapter
		this.dropdownView.setSelection(0)
		
		this.bindBehavior.onAttach = {}
		this.bindBehavior.onDispatchEvent = {
			it.setItem(this.selectedTrack)
		}
	}
	
	override fun onItemSelected(
		parent: AdapterView<*>?,
		view: View?,
		position: Int,
		id: Long
	)
	{
		val item = this.adapter.getItem(position)!!
		this.selectedItemTextView.text = item.name
		
		this.selectedTrack = item

		this.bindBehavior.requestDispatchEvent()
	}
	
	override fun onNothingSelected(parent: AdapterView<*>?)
	{
		throw NotImplementedError()
	}
	
	override fun attach(id: Int)
	{
		this.bindBehavior.attach(id)
	}

	override fun attach(view: View)
	{
		this.bindBehavior.attach(view)
	}

	override fun attach(fragment: Fragment)
	{
		this.bindBehavior.attach(fragment)
	}
	
	override fun attach(activity: FragmentActivity)
	{
		this.bindBehavior.attach(activity)
	}

	override fun attachAll(views: List<View>)
	{
		this.bindBehavior.attachAll(views)
	}

	override fun detach(id: Int)
	{
		this.bindBehavior.detach(id)
	}
	
	override fun detach(view: View)
	{
		this.bindBehavior.detach(view)
	}

	override fun detachAll()
	{
		this.bindBehavior.detachAll()
	}
	
	fun setCurrent(track: Track)
	{
		this.selectedTrack = track
		this.dropdownView.setSelection(this.tracks.indexOf(track))
	}
}