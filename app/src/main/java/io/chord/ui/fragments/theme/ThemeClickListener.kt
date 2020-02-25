package io.chord.ui.fragments.theme

import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ViewHolderBase

interface ThemeClickListener<TItem, THolder: ViewHolderBase<TItem, THolder>> : ClickListener<TItem, THolder>
{
    fun onPlayClicked(item: TItem)
}