package io.chord.ui.dialogs.customs

import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.ui.dialogs.DialogLevel
import io.chord.ui.dialogs.DialogParameters
import io.chord.ui.dialogs.FloatDialogFragment
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.cudc.CudcOperationInformation

class ConfirmationCudcOperationDialog(
	override val context: FragmentActivity,
	private val operation: CudcOperation,
	private val entityId: Int
): CustomDialog<(() -> Unit)>
{
	override val fragment: FloatDialogFragment
	
	override var onValidate: (() -> Unit)
		get() = this.fragment.onValidate
		set(value) {
			this.fragment.onValidate = value
		}
	
	init
	{
		val information = CudcOperationInformation(
			this.operation,
			this.context.getString(this.entityId)
		)
		
		this.fragment = FloatDialogFragment(
			DialogParameters(
				information.getTitle(),
				null,
				information.getOperationName(),
				this.context.getString(R.string.dialog_negative_button),
				DialogLevel.Warning
			)
		)
	}
	
	override fun show()
	{
		this.fragment.show(
			this.context.supportFragmentManager,
			"fragment_confirmation_cudc_operation_dialog"
		)
	}
}