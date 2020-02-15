package io.chord.ui.dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.chord.R
import kotlinx.android.synthetic.main.dialog_title.view.*

abstract class BaseDialogFragment(
	protected val parameters: DialogParameters
) : DialogFragment()
{
	private fun getColor(): Int
	{
		return when(this.parameters.level)
		{
			DialogLevel.Information -> this.context!!.getColor(R.color.colorAccent)
			DialogLevel.Error -> this.context!!.getColor(R.color.errorColor)
			DialogLevel.Warning -> this.context!!.getColor(R.color.warningColor)
		}
	}
	
	protected open fun getBuilder(): MaterialAlertDialogBuilder
	{
		return MaterialAlertDialogBuilder(this.requireContext(), R.style.Dialog)
	}
	
	@SuppressLint("InflateParams")
	protected open fun finalize(builder: MaterialAlertDialogBuilder): AlertDialog
	{
		builder.background = ContextCompat.getDrawable(
			this.context!!,
			R.drawable.layout_background_secondary
		)
		
		val titleView = LayoutInflater
			.from(this.context)
			.inflate(
				R.layout.dialog_title,
				null,
				false
			)
		
		titleView.setBackgroundColor(this.getColor())
		titleView.title.text = this.parameters.title
		builder.setCustomTitle(titleView)
		
		return builder.create()
	}
	
	override fun onStart()
	{
		super.onStart()
		
		val dialog = this.dialog as AlertDialog
		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(this.getColor())
		dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(this.getColor())
	}
}