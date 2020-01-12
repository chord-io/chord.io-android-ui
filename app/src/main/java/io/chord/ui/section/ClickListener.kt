package io.chord.ui.section

import androidx.databinding.ViewDataBinding

interface ClickListener<TItem, THolder: ViewHolderBase<TItem, THolder>>
{
    fun onItemClicked(item: TItem, holder: THolder)
    
    fun onItemLongClicked(item: TItem, holder: THolder): Boolean
    
    fun onFailedViewClicked(section: Section<TItem, THolder>)
}