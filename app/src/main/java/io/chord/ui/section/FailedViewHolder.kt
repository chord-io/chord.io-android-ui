package io.chord.ui.section

import android.view.View
import androidx.databinding.ViewDataBinding
import io.chord.R
import javax.validation.constraints.Null

class FailedViewHolder<TItem, THolder: ViewHolderBase<TItem, THolder>>(
    view: View
) : ViewHolderBase<TItem, THolder>(view)
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