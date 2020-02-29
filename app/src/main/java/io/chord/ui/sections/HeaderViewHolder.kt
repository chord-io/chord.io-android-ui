package io.chord.ui.sections

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class HeaderViewHolder<TItem, THolder: ViewHolder<TItem, THolder>>(
    view: View
) : RecyclerView.ViewHolder(view)
{
    abstract fun bind(
        title: String,
        section: Section<TItem, THolder>,
        clickListener: ClickListener<TItem, THolder>
    )
}