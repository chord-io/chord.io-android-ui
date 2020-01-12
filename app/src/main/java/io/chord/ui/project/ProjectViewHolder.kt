package io.chord.ui.project

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.view.IconicsImageView
import io.chord.R
import io.chord.client.models.Project
import io.chord.databinding.ProjectListItemBinding
import io.chord.ui.ChordIOApplication
import io.chord.ui.section.ClickListener
import io.chord.ui.section.ViewHolderBase

class ProjectViewHolder(
    view: View
) : ViewHolderBase<Project, ProjectViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ProjectListItemBinding>(this.itemView)!!
    
    override fun bind(item: Project, clickListener: ClickListener<Project, ProjectViewHolder>)
    {
        val visibilityIcon = if(item.isPrivate) FontAwesome.Icon.faw_eye_slash else FontAwesome.Icon.faw_eye
        
        binding.apply {
            this.icon.icon = IconicsDrawable(itemView.context)
                .icon(visibilityIcon)
                .colorRes(R.color.colorAccent)
    
            this.name.text = item.name
    
            this.information.text = ChordIOApplication.instance.resources.getString(
                R.string.project_list_item_information,
                item.tempo.toString(),
                item.tracks.size.toString()
            )
    
            this.loader.visibility = View.GONE
        }

        this.itemView.setOnClickListener {
            clickListener.onItemClicked(item, this)
        }

        this.itemView.setOnLongClickListener {
            clickListener.onItemLongClicked(item, this)
        }
    }
}