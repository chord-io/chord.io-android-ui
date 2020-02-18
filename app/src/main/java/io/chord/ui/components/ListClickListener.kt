package io.chord.ui.components

interface ListClickListener<TModel>
{
	fun onItemClicked(item: TModel)
	fun onDragEnded(from: Int, to: Int)
}