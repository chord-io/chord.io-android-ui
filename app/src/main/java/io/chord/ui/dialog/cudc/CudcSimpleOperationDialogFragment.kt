package io.chord.ui.dialog.cudc

import android.app.Dialog
import android.os.Bundle
import io.chord.R
import io.chord.ui.dialog.SimpleDialogFragment

class CudcSimpleOperationDialogFragment(
    private val information: CudcOperationInformation,
    private val onAcceptDialogListener: (() -> Unit)
) : SimpleDialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        this.title = this.information.getTitle()
        
        val builder = this.getBuilder()
        
        builder.setPositiveButton(information.getOperationName()) { _, _ -> run {
            this.onAcceptDialogListener.invoke()
        }}
        builder.setNegativeButton(R.string.cudc_dialog_negative_button, null)
        return this.finalize(builder)
    }
}