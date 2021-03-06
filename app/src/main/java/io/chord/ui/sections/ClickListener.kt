package io.chord.ui.sections

interface ClickListener<TItem, THolder: ViewHolder<TItem, THolder>>
{
    fun onItemClicked(item: TItem, holder: THolder)
    
    fun onItemLongClicked(item: TItem, holder: THolder): Boolean
    
    fun onFailedViewClicked(section: Section<TItem, THolder>)
}