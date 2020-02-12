package io.chord.ui.dialogs.customs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.ui.dialogs.DialogParameters
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.sections.SectionDialogFragment
import io.chord.ui.dialogs.sections.SectionDialogItem
import io.chord.ui.dialogs.sections.SectionDialogItemViewHolder
import io.chord.ui.sections.Section
import java.util.*

class SelectCudcOperationDialog(
	override val context: FragmentActivity,
	private val operations: EnumSet<CudcOperation>
) : CustomDialog<((CudcOperation) -> Unit)>
{
	override val fragment: DialogFragment
	override lateinit var onValidate: ((CudcOperation) -> Unit)
	
	lateinit var onCreateSelected: (() -> Unit)
	lateinit var onUpdateSelected: (() -> Unit)
	lateinit var onDeleteSelected: (() -> Unit)
	lateinit var onCloneSelected: (() -> Unit)
	
	init
	{
		this.fragment = SectionDialogFragment(
			DialogParameters(
				this.context,
				R.string.cudc_operation_dialog_title,
				null,
				R.string.cudc_operation_dialog_positive_button,
				R.string.cudc_operation_dialog_negative_button
			)
		)
		
		val section = Section(
			R.layout.section_dialog_item,
			{view -> SectionDialogItemViewHolder(view)},
			fragment
		)
		
		this.fragment.onValidate = {
			val operation = this.getEnumFromIndex(it.index)
			this.onValidate(operation)
			when(operation)
			{
				CudcOperation.CREATE -> this.onCreateSelected()
				CudcOperation.UPDATE -> this.onUpdateSelected()
				CudcOperation.DELETE -> this.onDeleteSelected()
				CudcOperation.CLONE -> this.onCloneSelected()
			}
		}
		
		this.fragment.addSection(section)
		
		this.operations.forEach {operation ->
			section.add(
				SectionDialogItem(
					operation.ordinal,
					this.getIconFromOperation(operation),
					this.getNameFromOperation(operation)
				)
			)
		}
	}
	
	private fun getNameFromOperation(operation: CudcOperation): String
	{
		return when(operation)
		{
			CudcOperation.CREATE -> this.context.getString(R.string.cudc_operation_dialog_create)
			CudcOperation.UPDATE -> this.context.getString(R.string.cudc_operation_dialog_update)
			CudcOperation.DELETE -> this.context.getString(R.string.cudc_operation_dialog_delete)
			CudcOperation.CLONE -> this.context.getString(R.string.cudc_operation_dialog_clone)
		}
	}
	
	private fun getIconFromOperation(operation: CudcOperation): String
	{
		return when(operation)
		{
			CudcOperation.CREATE -> "faw-plus"
			CudcOperation.UPDATE -> "faw-pen"
			CudcOperation.DELETE -> "faw-trash"
			CudcOperation.CLONE -> "faw-clone"
		}
	}
	
	private fun getEnumFromIndex(index: Int): CudcOperation
	{
		return CudcOperation.values()[index]
	}
	
	override fun show()
	{
		this.fragment.show(
			this.context.supportFragmentManager,
			"fragment_select_cudc_operation_dialog"
		)
	}
}