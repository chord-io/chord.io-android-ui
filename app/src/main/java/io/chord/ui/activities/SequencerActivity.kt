package io.chord.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeRes
import io.chord.R
import io.chord.databinding.ActivitySequencerBinding
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.ToolbarEditorBehavior
import io.chord.ui.fragments.theme.ThemeListFragment
import io.chord.ui.fragments.track.TrackListFragment

class SequencerActivity : AppCompatActivity()
{
	private lateinit var toolBarBehavior: ToolbarEditorBehavior
	private lateinit var binding: ActivitySequencerBinding
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		
		val view = LayoutInflater
			.from(this)
			.inflate(
				R.layout.activity_sequencer,
				null,
				false
			)
		
		this.binding = DataBindingUtil.bind(view)!!
		
		this.setSupportActionBar(this.binding.toolbarSequencer.editor.toolbar)
		this.setContentView(view)
		
		this.binding.horizontalScrollBar.attach(R.id.rulerScrollview)
		this.binding.horizontalScrollBar.attach(R.id.sequencerScrollview)
		
		this.binding.verticalScrollBar.attach(R.id.trackListScrollView)
		this.binding.verticalScrollBar.attach(R.id.sequencerScrollview)
		
		this.binding.horizontalZoomBar.attach(R.id.ruler)
		this.binding.horizontalZoomBar.attach(R.id.sequencer)
		
		this.binding.verticalZoomBar.attach(R.id.trackList)
		this.binding.verticalZoomBar.attach(R.id.sequencer)
		
		this.binding.toolbarSequencer.quantize.attach(R.id.ruler)
		this.binding.toolbarSequencer.quantize.attach(R.id.sequencer)
		
		this.binding.ruler.setCounter(this::counter)
		this.binding.sequencer.setCounter(this::counter)
		
		val themeList = this.supportFragmentManager.findFragmentById(R.id.themeList) as ThemeListFragment
		val trackList = this.supportFragmentManager.findFragmentById(R.id.trackList) as TrackListFragment
		trackList.attach(themeList)
		trackList.attach(this.binding.sequencer)
		
		this.binding.addTrackButton.setOnClickListener {
			trackList.create()
		}
		
		this.binding.library.addItemButton.setOnClickListener {
			themeList.create()
		}
		
		this.toolBarBehavior = ToolbarEditorBehavior(
			this,
			this.binding.toolbarSequencer.editor
		)
		
		this.toolBarBehavior.onModeChanged = {
			this.binding.sequencer.setMode(it)
			this.binding.sequencerScrollview.setMode(it)
		}
		this.toolBarBehavior.onPlay = {}
		this.toolBarBehavior.onStop = {}
		this.toolBarBehavior.onUndo = {}
		this.toolBarBehavior.onRedo = {}
		
		this.binding.toolbarSequencer.library.setOnClickListener {
			if(this.binding.libraryContainer.visibility == View.VISIBLE)
			{
				this.binding.libraryContainer.visibility = View.GONE
			}
			else
			{
				this.binding.libraryContainer.visibility = View.VISIBLE
			}
		}
		
		this.binding.toolbarSequencer.editor.toolbar.navigationIcon = IconicsDrawable(this)
			.icon(FontAwesome.Icon.faw_arrow_left)
			.sizeRes(R.dimen.app_bar_icon_size)
			.colorRes(R.color.backgroundPrimary)
		
		this.binding.toolbarSequencer.editor.toolbar.setNavigationOnClickListener {
			this.onBackPressed()
		}
		
		this.binding.toolbarSequencer.editor.toolbar.setTitle(R.string.sequencer_activity_title)
	}
	
	private fun counter(): List<Int>
	{
		return ProjectManager.getCurrent()!!.tracks.flatMap { track ->
			track.entries.map {
				it.length.end.toInt()
			}
		}
	}
}
