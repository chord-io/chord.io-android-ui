package io.chord.ui.dialogs.customs

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import io.chord.ui.dialogs.FullscreenDialogFragment
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.cudc.CudcOperationInformation

class FormCudcOperationDialog<TBinding: ViewDataBinding>(
	override val context: FragmentActivity,
	private val operation: CudcOperation,
	private val entity: String,
	private val layoutId: Int
	): CustomDialog<((dataBinding: TBinding) -> Unit)>
{
	override val fragment: FullscreenDialogFragment<TBinding>
	
	lateinit var onCreate: ((dataBinding: TBinding) -> Unit)
	override lateinit var onValidate: ((dataBinding: TBinding) -> Unit)
	lateinit var onBind: ((dataBinding: TBinding) -> Unit)
	
	init
	{
		val information = CudcOperationInformation(
			this.operation,
			this.entity
		)
		
		this.fragment = FullscreenDialogFragment(
			this.layoutId,
			information.getTitle()
		)
		
		this.fragment.onCreate = this.onCreate
		this.fragment.onValidate = this.onValidate
		this.fragment.onBind = this.onBind
	}
	
	override fun show()
	{
		this.fragment.show(
			this.context.supportFragmentManager,
			"fragment_form_cudc_operation_dialog"
		)
	}
}