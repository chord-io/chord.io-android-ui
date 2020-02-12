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
	
	lateinit var onCreate: ((dataBinding: TBinding) -> Unit)
	override lateinit var onValidate: ((dataBinding: TBinding) -> Unit)
	lateinit var onBind: ((dataBinding: TBinding) -> Unit)
	
	init
	{
		this.fragment.onCreate = this.onCreate
		this.fragment.onValidate = this.onValidate
		this.fragment.onBind = this.onBind
	}
	
	override fun show()
	{
		this.fragment.show(
			this.context.supportFragmentManager,
			"fragment_form_dialog"
		)
	}
}