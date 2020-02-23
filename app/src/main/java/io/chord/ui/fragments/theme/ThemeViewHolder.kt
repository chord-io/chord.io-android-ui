package io.chord.ui.fragments.theme

import android.view.View
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.colorRes
import io.chord.R
import io.chord.clients.models.Theme
import io.chord.databinding.ThemeListItemBinding
import io.chord.ui.models.ThemeListItemViewModel
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ViewHolderBase

class ThemeViewHolder(
    view: View
) : ViewHolderBase<Theme, ThemeViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ThemeListItemBinding>(this.itemView)!!
    
    override fun bind(item: Theme, clickListener: ClickListener<Theme, ThemeViewHolder>)
    {
        this.binding.icon.icon = IconicsDrawable(this.itemView.context)
            .icon("faw-play")
            .colorRes(R.color.colorAccent)
        
        this.binding.theme = ThemeListItemViewModel(item)

        this.itemView.setOnClickListener {
            clickListener.onItemClicked(item, this)
        }

        this.itemView.setOnLongClickListener {
            clickListener.onItemLongClicked(item, this)
        }
    }
}