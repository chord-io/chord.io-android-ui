package io.chord.ui.project

import io.chord.client.models.Project
import io.chord.ui.utils.ClickListener

interface ClickListener : ClickListener
{
    fun onItemClicked(project: Project)

    fun onItemLongClicked(project: Project): Boolean
}