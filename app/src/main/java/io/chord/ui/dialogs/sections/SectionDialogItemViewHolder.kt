package io.chord.ui.dialogs.sections

import android.view.View
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeRes
import io.chord.R
import io.chord.databinding.SectionDialogItemBinding
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ViewHolder

class SectionDialogItemViewHolder(
    view: View
) : ViewHolder<SectionDialogItem, SectionDialogItemViewHolder>(view)
{
    val binding = DataBindingUtil.bind<SectionDialogItemBinding>(this.itemView)!!

    override fun bind(item: SectionDialogItem, clickListener: ClickListener<SectionDialogItem, SectionDialogItemViewHolder>)
    {
        this.binding.item = item

        if(item.icon == null)
        {
            this.binding.icon.visibility = View.GONE
        }

        binding.apply {
            if(item.icon != null)
            {
                this.icon.icon = IconicsDrawable(itemView.context)
                    .icon(item.icon!!)
                    .colorRes(R.color.colorAccent)
                    .sizeRes(R.dimen.section_dialog_icon_size)
            }

            this.name.text = item.name
        }

        this.binding.name.setOnClickListener {
            clickListener.onItemClicked(item, this)
        }

        this.binding.name.setOnLongClickListener {
            clickListener.onItemLongClicked(item, this)
        }
    }
}