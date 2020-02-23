package io.chord.ui.sections

import android.view.View

abstract class HeaderViewHolder<TItem, THolder: ViewHolderBase<TItem, THolder>>(
    view: View
) : ViewHolderBase<TItem, THolder>(view)
{
    override fun bind(
        item: TItem,
        clickListener: ClickListener<TItem, THolder>
    )
    {
        throw NotImplementedError()
    }
    
    abstract fun bind(
        title: String,
        clickListener: ClickListener<TItem, THolder>
    )
}