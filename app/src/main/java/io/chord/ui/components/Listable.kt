package io.chord.ui.components

import io.chord.ui.behaviors.Bindable

interface Listable<TModel> : Bindable
{
	fun setDataSet(dataset: List<TModel>)
}