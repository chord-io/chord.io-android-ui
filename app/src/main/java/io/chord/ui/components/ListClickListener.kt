package io.chord.ui.components

interface ListClickListener<TModel>
{
	fun onItemClicked(item: TModel)
	fun onItemLongClicked(item: TModel): Boolean
}