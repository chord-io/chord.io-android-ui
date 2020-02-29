package io.chord.ui.sections

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolder<TItem, THolder: ViewHolder<TItem, THolder>>(
	view: View
) : RecyclerView.ViewHolder(view)
{
	abstract fun bind(item: TItem, clickListener: ClickListener<TItem, THolder>)
}