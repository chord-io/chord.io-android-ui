package io.chord.ui.dialog.cudc

import android.os.Bundle
import android.view.View
import io.chord.ui.dialog.FullscreenDialogFragment

class CudcFormOperationDialogFragment(
	private val information: CudcOperationInformation,
	layoutId: Int
) : FullscreenDialogFragment(layoutId, loaderOnAccept = true)
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