package io.chord.ui.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section

class FailedViewHolder(view: View) : RecyclerView.ViewHolder(view)
{
    val rootView: View = this.itemView.findViewById(R.id.rootView)
    
    fun bind(section: Section, clickListener: ClickListener)
    {
        this.rootView.setOnClickListener {
            clickListener.onFailedViewClicked(section)
        }
    }
}