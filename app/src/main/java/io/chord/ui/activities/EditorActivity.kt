package io.chord.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeRes
import io.chord.R
import io.chord.clients.models.Theme
import io.chord.clients.models.Track
import io.chord.databinding.ActivityEditorBinding
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.ToolbarEditorBehavior

class EditorActivity : AppCompatActivity()
{
	private val tracks = ProjectManager.getCurrent()!!.tracks
	private lateinit var track: Track
	private lateinit var theme: Theme
	private lateinit var toolBarBehavior: ToolbarEditorBehavior
	private lateinit var binding: ActivityEditorBinding
	
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
		
		this.binding = DataBindingUtil.bind(view)!!
		
		this.setSupportActionBar(this.binding.toolbarEditor.editor.toolbar)
		this.setContentView(view)
		
		this.processIntent()
		
//		this.binding.horizontalScrollBar.attach(R.id.rulerScrollview)
//		this.binding.horizontalScrollBar.attach(R.id.sequencerScrollview)
//
//		this.binding.verticalScrollBar.attach(R.id.trackListScrollView)
//		this.binding.verticalScrollBar.attach(R.id.sequencerScrollview)
		this.binding.verticalScrollBar.attach(R.id.keyboardListScrollview)

		this.binding.horizontalZoomBar.attach(R.id.ruler)
//		this.binding.horizontalZoomBar.attach(R.id.keyboard)
//		this.binding.horizontalZoomBar.attach(R.id.sequencer)
//		this.binding.horizontalZoomBar.attach(R.id.keyboardListTest)
//
//		this.binding.verticalZoomBar.attach(R.id.trackList)
//		this.binding.verticalZoomBar.attach(R.id.sequencer)
//		this.binding.verticalZoomBar.attach(R.id.keyboard)
		this.binding.verticalZoomBar.attach(R.id.keyboardList)
		this.binding.verticalZoomBar.attach(R.id.keyboardListTest)
		
		this.binding.toolbarEditor.quantize.attach(R.id.ruler)
//		this.binding.toolbarEditor.quantize.attach(R.id.sequencer)
		
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
	
	private fun processIntent()
	{
		this.track = this.getTrack(this.intent.getStringExtra("track"))
		this.theme = this.getTheme(this.track, this.intent.getStringExtra("theme"))
		this.binding.toolbarEditor.track.setCurrent(this.track)
	}
	
	private fun onTrackChanged(track: Track)
	{
		this.track = track
		this.theme = this.getTheme(this.track, this.theme.name)
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
	
	private fun counter(): List<Int>
	{
		return this.theme.sequences.map {
			it.length.end.toInt()
		}
	}
}