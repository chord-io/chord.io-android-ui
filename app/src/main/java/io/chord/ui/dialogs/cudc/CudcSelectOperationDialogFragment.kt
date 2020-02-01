package io.chord.ui.dialogs.cudc

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import io.chord.R
import io.chord.ui.dialogs.SimpleDialogFragment
import kotlinx.android.synthetic.main.cudc_select_operation_dialog_fragment.view.*
import java.util.*

class CudcSelectOperationDialogFragment(
    private val operations: EnumSet<CudcOperation>
) : SimpleDialogFragment()
{
    lateinit var onCreateSelectedListener: (() -> Unit)
    lateinit var onUpdateSelectedListener: (() -> Unit)
    lateinit var onDeleteSelectedListener: (() -> Unit)
    lateinit var onCloneSelectedListener: (() -> Unit)
    
    private var rootView: View? = null
    
    private fun getSelectedOperation(): CudcOperation?
    {
        when
        {
            this.rootView!!.create.isChecked ->
            {
                return CudcOperation.CREATE
            }
            this.rootView!!.update.isChecked ->
            {
                return CudcOperation.UPDATE
            }
            this.rootView!!.delete.isChecked ->
            {
                return CudcOperation.DELETE
            }
            this.rootView!!.clone.isChecked ->
            {
                return CudcOperation.CLONE
            }
        }
        
        return null
    }
    
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val view = LayoutInflater
            .from(this.context)
            .inflate(
                R.layout.cudc_select_operation_dialog_fragment,
                null,
                false
            )
    
        if(!this.operations.contains(CudcOperation.CREATE))
        {
            view.createIcon.visibility = View.GONE
            view.create.visibility = View.GONE
        }
    
        if(!this.operations.contains(CudcOperation.UPDATE))
        {
            view.updateIcon.visibility = View.GONE
            view.update.visibility = View.GONE
        }
    
        if(!this.operations.contains(CudcOperation.DELETE))
        {
            view.deleteIcon.visibility = View.GONE
            view.delete.visibility = View.GONE
        }
    
        if(!this.operations.contains(CudcOperation.CLONE))
        {
            view.cloneIcon.visibility = View.GONE
            view.clone.visibility = View.GONE
        }
        
        this.rootView = view
        
        val builder = this.getBuilder()
        this.title = this.getString(R.string.cudc_dialog_title)
        builder.setView(view)
        builder.setPositiveButton(R.string.cudc_dialog_positive_button) { _, _ -> run {
            when(this.getSelectedOperation())
            {
                CudcOperation.CREATE ->
                {
                    this.onCreateSelectedListener.invoke()
                }
                CudcOperation.UPDATE ->
                {
                    this.onUpdateSelectedListener.invoke()
                }
                CudcOperation.DELETE ->
                {
                    this.onDeleteSelectedListener.invoke()
                }
                CudcOperation.CLONE ->
                {
                    this.onCloneSelectedListener.invoke()
                }
                else -> null
            }
        }}
        builder.setNegativeButton(R.string.cudc_dialog_negative_button, null)
        return this.finalize(builder)
    }
}