package io.chord.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.chord.R
import kotlinx.android.synthetic.main.dialog_title.view.*

open class ErrorDialogFragment(
	private val messageId: Int
) : SimpleDialogFragment()
{
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
	{
		this.title = this.getString(R.string.error_dialog_title)
		
		val builder = this.getBuilder()
		
		builder.setMessage(this.messageId)
		
		builder.setPositiveButton(R.string.error_dialog_positive_button, null)
		return this.finalize(builder)
	}

	override fun finalize(builder: MaterialAlertDialogBuilder): AlertDialog
	{
		builder.background = ContextCompat.getDrawable(
			this.activity!!,
			R.drawable.layout_background_secondary
		)
		
		val titleView = this.activity!!.layoutInflater.inflate(R.layout.dialog_error_title, null)
		titleView.title.text = this.title
		builder.setCustomTitle(titleView)
		
		return builder.create()
	}
}