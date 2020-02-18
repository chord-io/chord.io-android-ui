package io.chord.ui.components

import android.view.View

interface ListViewHolder<TModel, TViewModel: ListViewModel>
{
	val view: View
	var model: TModel
	fun bind(model: TViewModel, listener: ListClickListener<TModel>)
	fun unbind()
}