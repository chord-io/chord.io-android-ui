package io.chord.ui.sections

import android.view.View
import io.chord.R

class FailedViewHolder<TItem, THolder: ViewHolder<TItem, THolder>>(
    view: View
) : ViewHolder<TItem, THolder>(view)
{
    private val rootView: View = this.itemView.findViewById(R.id.rootView)
    
    override fun bind(
        item: TItem,
        clickListener: ClickListener<TItem, THolder>
    )
    {
        throw NotImplementedError()
    }
    
    fun bind(
        section: Section<TItem, THolder>,
        clickListener: ClickListener<TItem, THolder>
    )
    {
        this.rootView.setOnClickListener {
            clickListener.onFailedViewClicked(section)
        }
    }
}