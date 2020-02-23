package io.chord.ui.fragments.theme

import android.view.View
import androidx.databinding.DataBindingUtil
import io.chord.clients.models.Theme
import io.chord.databinding.ThemeListHeaderBinding
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.HeaderViewHolder

class ThemeHeaderViewHolder(
    view: View
) : HeaderViewHolder<Theme, ThemeViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ThemeListHeaderBinding>(this.itemView)!!
    
    override fun bind(
        item: Theme,
        clickListener: ClickListener<Theme, ThemeViewHolder>
    )
    {
        throw NotImplementedError()
    }
    
    override fun bind(
        title: String,
        clickListener: ClickListener<Theme, ThemeViewHolder>
    )
    {
        this.binding.title.text = title
    }
}