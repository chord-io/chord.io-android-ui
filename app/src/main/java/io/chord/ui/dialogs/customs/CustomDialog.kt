package io.chord.ui.dialogs.customs

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

interface CustomDialog<T>
{
	val context: FragmentActivity
	val fragment: DialogFragment
	var onValidate: T
	
	fun show()
}