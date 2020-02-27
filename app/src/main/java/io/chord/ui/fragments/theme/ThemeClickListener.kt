package io.chord.ui.fragments.theme

import io.chord.ui.sections.ClickListener

interface ThemeClickListener : ClickListener<ThemeSectionItem, ThemeViewHolder>
{
    fun onPlayClicked(item: ThemeSectionItem)
    fun onStopClicked(item: ThemeSectionItem)
}