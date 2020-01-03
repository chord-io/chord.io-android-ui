package io.chord.ui.project

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.client.models.Project

class ProjectViewHolder(view: View) : RecyclerView.ViewHolder(view)
{
    var model: Project? = null
    val name: TextView = this.itemView.findViewById(R.id.name)

    fun bind(project: Project, clickListener: ClickListener)
    {
        this.model = project
        this.name.text = project.name

        this.itemView.setOnClickListener {
            clickListener.onItemClicked(this.model!!)
        }

        this.itemView.setOnLongClickListener {
            clickListener.onItemLongClicked(this.model!!)
        }
    }
}