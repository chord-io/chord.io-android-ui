package io.chord.ui.dialog.cudc

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import io.chord.ui.dialog.FullscreenDialogFragment

class CudcFormOperationDialogFragment<TBinding: ViewDataBinding>(
	private val information: CudcOperationInformation,
	layoutId: Int
) : FullscreenDialogFragment<TBinding>(layoutId)
{
	override fun onViewCreated(
		view: View,
		savedInstanceState: Bundle?
	)
	{
		this.title = this.information.getTitle()
		super.onViewCreated(view, savedInstanceState)
	}
}