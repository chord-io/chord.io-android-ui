package io.chord.ui.behaviors

import android.content.Context
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.backgroundColorRes
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.paddingDp
import com.mikepenz.iconics.utils.roundedCornersDp
import com.mikepenz.iconics.utils.sizeRes
import com.mikepenz.iconics.view.IconicsImageView
import io.chord.R
import io.chord.databinding.ToolbarEditorBinding
import io.chord.ui.extensions.getChildOfType

class ToolbarEditorBehavior(
	private val context: Context,
	private val binding: ToolbarEditorBinding)
{
	private var isPlaying: Boolean = false
	
	lateinit var onMoveMode: (() -> Unit)
	lateinit var onSelectMode: (() -> Unit)
	lateinit var onEditMode: (() -> Unit)
	lateinit var onEraseMode: (() -> Unit)
	lateinit var onCloneMode: (() -> Unit)
	lateinit var onPlay: (() -> Unit)
	lateinit var onStop: (() -> Unit)
	lateinit var onUndo: (() -> Unit)
	lateinit var onRedo: (() -> Unit)
	
	init
	{
		val modes = this.binding.modes.getChildOfType<IconicsImageView>()
		modes.forEach { mode ->
			mode.setOnClickListener { view ->
				modes.forEach {
					this.setUnselectedIconColor(it)
				}
				this.setSelectedIconColor(mode)
				when(view.id)
				{
					R.id.move -> this.onMoveMode()
					R.id.select -> this.onSelectMode()
					R.id.edit -> this.onEditMode()
					R.id.erase -> this.onEraseMode()
					R.id.clone -> this.onCloneMode()
				}
			}
		}
		
		this.binding.control.setOnClickListener {
			if(!this.isPlaying)
			{
				this.setIcon(it as IconicsImageView, "faw-stop")
				this.isPlaying = true
				this.onPlay()
			}
			else
			{
				this.setIcon(it as IconicsImageView, "faw-play")
				this.isPlaying = false
				this.onStop()
			}
		}
	}
	
	private fun setUnselectedIconColor(view: IconicsImageView)
	{
		view.icon = IconicsDrawable(this.context)
			.icon(view.icon!!.icon!!)
			.sizeRes(R.dimen.app_bar_icon_size)
			.colorRes(R.color.backgroundPrimary)
	}
	
	private fun setSelectedIconColor(view: IconicsImageView)
	{
		view.icon = IconicsDrawable(this.context)
			.icon(view.icon!!.icon!!)
			.sizeRes(R.dimen.app_bar_icon_size)
			.colorRes(R.color.colorAccent)
			.backgroundColorRes(R.color.backgroundPrimary)
			.roundedCornersDp(3)
			.paddingDp(4)
	}
	
	private fun setIcon(view: IconicsImageView, icon: String)
	{
		view.icon!!.icon(icon)
	}
}