package io.chord.ui.dialogs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.chord.R
import kotlinx.android.synthetic.main.dialog_title.view.*

open class SimpleDialogFragment : DialogFragment()
{
	protected lateinit var title: String
	
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
		titleView.title.text = this.title
		builder.setCustomTitle(titleView)
		
		return builder.create()
	}
}