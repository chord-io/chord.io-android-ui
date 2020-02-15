package io.chord.ui.dialogs.customs

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import io.chord.ui.dialogs.FullscreenDialogFragment

class FormDialog<TBinding: ViewDataBinding>(
	override val context: FragmentActivity,
	private val entity: String,
	private val layoutId: Int
	): CustomDialog<((dataBinding: TBinding) -> Unit)>
{
	override val fragment: FullscreenDialogFragment<TBinding> = FullscreenDialogFragment(
		this.layoutId,
		this.entity
	)
	
	override var onValidate: ((dataBinding: TBinding) -> Unit)
		get() = this.fragment.onValidate
		set(value) {
			this.fragment.onValidate = value
		}
	
	var onBind: ((dataBinding: TBinding) -> Unit)
		get() = this.fragment.onBind
		set(value) {
			this.fragment.onBind = value
		}
	
	override fun show()
	{
		this.fragment.show(
			this.context.supportFragmentManager,
			"fragment_form_dialog"
		)
	}
}