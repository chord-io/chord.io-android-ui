package io.chord.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeRes
import io.chord.R
import io.chord.clients.models.MidiTrack
import io.chord.clients.models.Theme
import io.chord.clients.models.Track
import io.chord.databinding.ActivityEditorBinding
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.FragmentTransactionManager
import io.chord.ui.behaviors.ToolbarEditorBehavior
import io.chord.ui.components.Selectable
import io.chord.ui.fragments.editor.PianoFragmentTransaction

class EditorActivity : AppCompatActivity(), Selectable<Track>
{
	private class LaneScrollViewListener(
		private val activity: EditorActivity
	) : ViewGroup.OnHierarchyChangeListener
	{
		override fun onChildViewAdded(parent: View, child: View)
		{
			child.post {
				this.activity.binding.verticalScrollBar.attach(R.id.laneScrollview)
			}
		}
		
		override fun onChildViewRemoved(parent: View, child: View)
		{
			try
			{
				this.activity.binding.verticalScrollBar.detach(R.id.laneScrollview)
			}
			catch(exception: KotlinNullPointerException){}
		}
	}
	
	private class RollScrollViewListener(
		private val activity: EditorActivity
	) : ViewGroup.OnHierarchyChangeListener
	{
		override fun onChildViewAdded(parent: View, child: View)
		{
			child.post {
				this.activity.binding.verticalScrollBar.attach(R.id.rollScrollview)
				this.activity.binding.horizontalScrollBar.attach(R.id.rollScrollview)
			}
		}
		
		override fun onChildViewRemoved(parent: View, child: View)
		{
			try
			{
				this.activity.binding.verticalScrollBar.detach(R.id.rollScrollview)
				this.activity.binding.horizontalScrollBar.detach(R.id.rollScrollview)
			}
			catch(exception: KotlinNullPointerException){}
		}
	}
	
	private val tracks = ProjectManager.getCurrent()!!.tracks
	private lateinit var track: Track
	private lateinit var _theme: Theme
	private lateinit var toolBarBehavior: ToolbarEditorBehavior
	private lateinit var _binding: ActivityEditorBinding
	private val bindableBehavior = BindableBehavior(this)
	private val transactions = FragmentTransactionManager()
	
	val binding: ActivityEditorBinding
		get() = this._binding
	
	val theme: Theme
		get() = this._theme
	
	init
	{
		this.transactions.add(MidiTrack::class, PianoFragmentTransaction(this))
	}
	
	override fun attach(controller: BindBehavior<Bindable>)
	{
		this.bindableBehavior.attach(controller)
	}
	
	override fun selfAttach()
	{
		this.bindableBehavior.selfAttach()
	}
	
	override fun selfDetach()
	{
		this.bindableBehavior.selfDetach()
	}
	
	override fun setItem(item: Track)
	{
		this.onTrackChanged(item)
	}
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		
		val view = LayoutInflater
			.from(this)
			.inflate(
				R.layout.activity_editor,
				null,
				false
			)
		
		this._binding = DataBindingUtil.bind(view)!!
		
		this.setSupportActionBar(this.binding.toolbarEditor.editor.toolbar)
		this.setContentView(view)
		
		this.processIntent()
		
		this.binding.horizontalScrollBar.attach(R.id.rulerScrollview)
		
		this.binding.horizontalZoomBar.attach(R.id.ruler)
		
		this.binding.toolbarEditor.quantize.attach(R.id.ruler)
		
		this.binding.toolbarEditor.track.attach(this)
		
		this.binding.ruler.setCounter(this::counter)
		
		this.toolBarBehavior = ToolbarEditorBehavior(
			this,
			this.binding.toolbarEditor.editor
		)
		
		this.toolBarBehavior.onModeChanged = {
		}
		this.toolBarBehavior.onPlay = {}
		this.toolBarBehavior.onStop = {}
		this.toolBarBehavior.onUndo = {}
		this.toolBarBehavior.onRedo = {}
		
		//		this.binding.toolbarRoller.library.setOnClickListener {
		//			if(this.binding.libraryContainer.visibility == View.VISIBLE)
		//			{
		//				this.binding.libraryContainer.visibility = View.GONE
		//			}
		//			else
		//			{
		//				this.binding.libraryContainer.visibility = View.VISIBLE
		//			}
		//		}
		
		this.binding.toolbarEditor.editor.toolbar.navigationIcon = IconicsDrawable(this)
			.icon(FontAwesome.Icon.faw_arrow_left)
			.sizeRes(R.dimen.app_bar_icon_size)
			.colorRes(R.color.backgroundPrimary)
		
		this.binding.toolbarEditor.editor.toolbar.setNavigationOnClickListener {
			this.onBackPressed()
		}
		
		this.binding.toolbarEditor.editor.toolbar.setTitle(R.string.editor_activity_title)
	}
	
	override fun onAttachedToWindow()
	{
		super.onAttachedToWindow()
		this.binding.laneScrollview.setOnHierarchyChangeListener(LaneScrollViewListener(this))
		this.binding.rollScrollview.setOnHierarchyChangeListener(RollScrollViewListener(this))
	}
	
	fun counter(): List<Int>
	{
		return this.theme.sequences.map {
			it.length.end.toInt()
		}
	}
	
	private fun processIntent()
	{
		this.track = this.getTrack(this.intent.getStringExtra("track"))
		this._theme = this.getTheme(this.track, this.intent.getStringExtra("theme"))
		this.binding.toolbarEditor.track.setCurrent(this.track)
	}
	
	private fun onTrackChanged(track: Track)
	{
		this.transactions.from(this.track).unload(this.supportFragmentManager)
		this.track = track
		this._theme = this.getTheme(this.track, this.theme.name)
		this.transactions.from(this.track).load(this.supportFragmentManager)
	}
	
	private fun getTrack(name: String): Track
	{
		return this.tracks.first {
			it.name == name
		}
	}
	
	private fun getTheme(track: Track, name: String): Theme
	{
		return track.themes.first {
			it.name == name
		}
	}
}