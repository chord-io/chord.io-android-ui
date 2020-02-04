package io.chord.ui.components

interface TrackListClickListener
{
    fun onItemClicked(item: TrackListItem)
    
    fun onItemLongClicked(item: TrackListItem): Boolean
}