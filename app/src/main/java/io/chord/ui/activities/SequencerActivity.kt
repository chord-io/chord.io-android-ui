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
import io.chord.ui.behaviors.ToolbarEditorBehavior
import io.chord.ui.extensions.addIcon
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
		
		this.binding.toolbarSequencer.editor.toolbar
		this.binding.horizontalScrollBar.attach(R.id.rulerScrollview)
		
		this.binding.verticalScrollBar.attach(R.id.trackListScrollView)
		this.binding.verticalScrollBar.attach(R.id.sequencerScrollview)
		
		this.binding.horizontalZoomBar.attach(R.id.ruler)
		
		this.binding.verticalZoomBar.attach(R.id.trackList)
		this.binding.verticalZoomBar.attach(R.id.sequencer)
		
		this.binding.toolbarSequencer.quantize.attach(R.id.ruler)
		this.binding.toolbarSequencer.quantize.attach(R.id.sequencer)
		
		this.setSupportActionBar(this.binding.toolbarSequencer.editor.toolbar)
		this.setContentView(view)
		
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
		
		this.binding.addBarButton.addIcon(this)
		
		this.toolBarBehavior = ToolbarEditorBehavior(
			this,
			this.binding.toolbarSequencer.editor
		)
		
		this.toolBarBehavior.onMoveMode = {}
		this.toolBarBehavior.onSelectMode = {}
		this.toolBarBehavior.onEditMode = {}
		this.toolBarBehavior.onEraseMode = {}
		this.toolBarBehavior.onCloneMode = {}
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
	}
}
