package io.chord.ui.extensions

import android.content.Context
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import io.chord.R

fun FloatingActionButton.setIcon(
	context: Context,
	icon: IIcon,
	colorId: Int
)
{
	this.setImageDrawable(
		IconicsDrawable(context)
			.icon(icon)
			.colorRes(colorId)
	)
}

fun FloatingActionButton.addIcon(context: Context)
{
	this.setIcon(
		context,
		FontAwesome.Icon.faw_plus,
		R.color.backgroundPrimary
	)
}