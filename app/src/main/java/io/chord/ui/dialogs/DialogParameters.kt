package io.chord.ui.dialogs

import android.content.Context

class DialogParameters
{
	val title: String
	val message: String?
	val positiveButtonText: String
	val negativeButtonText: String
	val level: DialogLevel
	
	constructor(
		context: Context,
		titleId: Int,
		messageId: Int?,
		positiveButtonTextId: Int,
		negativeButtonTextId: Int,
		level: DialogLevel = DialogLevel.Information
	)
	{
		this.title = context.resources.getString(titleId)
		this.message = if(messageId != null) context.resources.getString(messageId) else null
		this.positiveButtonText = context.resources.getString(positiveButtonTextId)
		this.negativeButtonText = context.resources.getString(negativeButtonTextId)
		this.level = level
	}
	
	constructor(
		title: String,
		message: String?,
		positiveButtonText: String,
		negativeButtonText: String,
		level: DialogLevel = DialogLevel.Information
	)
	{
		this.title = title
		this.message = message
		this.positiveButtonText = positiveButtonText
		this.negativeButtonText = negativeButtonText
		this.level = level
	}
}