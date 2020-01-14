package io.chord.ui.sections

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ViewHolderBase<TItem, THolder: ViewHolderBase<TItem, THolder>>(
	itemView: View
) : RecyclerView.ViewHolder(itemView)
{
	abstract fun bind(item: TItem, clickListener: ClickListener<TItem, THolder>)
}