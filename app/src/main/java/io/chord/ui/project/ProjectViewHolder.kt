package io.chord.ui.project

import android.view.View
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import io.chord.R
import io.chord.clients.models.Project
import io.chord.clients.models.Visibility
import io.chord.databinding.ProjectListItemBinding
import io.chord.ui.ChordIOApplication
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ViewHolderBase

class ProjectViewHolder(
    view: View
) : ViewHolderBase<Project, ProjectViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ProjectListItemBinding>(this.itemView)!!
    
    override fun bind(item: Project, clickListener: ClickListener<Project, ProjectViewHolder>)
    {
        val visibilityIcon = if(item.visibility == Visibility.Private) FontAwesome.Icon.faw_eye_slash else FontAwesome.Icon.faw_eye
        
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