package io.chord.ui.components

interface TrackListClickListener
{
    fun onItemClicked(item: TrackListItemViewHolder)
    
    fun onItemLongClicked(item: TrackListItemViewHolder): Boolean
}