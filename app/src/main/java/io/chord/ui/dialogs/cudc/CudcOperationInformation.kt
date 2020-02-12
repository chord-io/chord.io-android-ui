package io.chord.ui.dialogs.cudc

import io.chord.R
import io.chord.ui.ChordIOApplication

class CudcOperationInformation(
	private val operation: CudcOperation,
	private val entity: String
)
{
	fun getOperationName(): String
	{
		when(this.operation)
		{
			CudcOperation.CREATE ->
			{
				return ChordIOApplication.instance.resources.getString(R.string.cudc_operation_dialog_create)
			}
			CudcOperation.UPDATE ->
			{
				return ChordIOApplication.instance.resources.getString(R.string.cudc_operation_dialog_update)
			}
			CudcOperation.DELETE ->
			{
				return ChordIOApplication.instance.resources.getString(R.string.cudc_operation_dialog_delete)
			}
			CudcOperation.CLONE ->
			{
				return ChordIOApplication.instance.resources.getString(R.string.cudc_operation_dialog_clone)
			}
		}
	}
	
	fun getTitle(): String
	{
		val operationName = this.getOperationName()
		
		when(this.operation)
		{
			CudcOperation.CREATE ->
			{
				return "$operationName $entity"
			}
			CudcOperation.UPDATE ->
			{
				return "$operationName $entity"
			}
			CudcOperation.DELETE ->
			{
				return "$operationName $entity ?"
			}
			CudcOperation.CLONE ->
			{
				return "$operationName $entity ?"
			}
		}
	}
}