package io.chord.ui.dialogs

import android.app.Dialog
import android.os.Bundle

open class FloatDialogFragment(
	parameters: DialogParameters
) : BaseDialogFragment(parameters)
{
	open lateinit var onValidate: (() -> Unit)
	
	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
	{
		val builder = super.getBuilder()
		
		if(this.parameters.message != null)
		{
			builder.setMessage(this.parameters.message)
		}
		
		builder.setPositiveButton(this.parameters.positiveButtonText) {_, _ -> run {
			this.onValidate()
		}}
		
		builder.setNegativeButton(this.parameters.negativeButtonText, null)
		
		return this.finalize(builder)
	}
}