package io.chord.ui.sections

import android.view.View

class ExpandableSection<TItem, THolder: ViewHolderBase<TItem, THolder>>: Section<TItem, THolder>
{
	private var _isExpanded = true
	private var items: MutableList<TItem>? = null
	
	var isExpanded: Boolean
		get() = this._isExpanded
		set(value) {
			this._isExpanded = value
		}
	
	constructor(
		title: String,
		itemResourceId: Int,
		headerResourceId: Int,
		emptyResourceId: Int,
		loadingResourceId: Int,
		failedResourceId: Int,
		holderFactory: (view: View) -> THolder,
		headerHolderFactory: ((view: View) -> HeaderViewHolder<TItem, THolder>)?,
		clickListener: ClickListener<TItem, THolder>
	) : super(
		title,
		itemResourceId,
		headerResourceId,
		emptyResourceId,
		loadingResourceId,
		failedResourceId,
		holderFactory,
		headerHolderFactory,
		clickListener
	)
	
	constructor(
		itemResourceId: Int,
		headerResourceId: Int,
		holderFactory: (view: View) -> THolder,
		clickListener: ClickListener<TItem, THolder>
	) : super(
		itemResourceId,
		headerResourceId,
		holderFactory,
		clickListener
	)
	
	constructor(
		itemResourceId: Int,
		holderFactory: (view: View) -> THolder,
		clickListener: ClickListener<TItem, THolder>
	) : super(
		itemResourceId,
		holderFactory,
		clickListener
	)
	
	override fun getContentItemsTotal(): Int
	{
		return if(this.isExpanded)
		{
			super.getContentItemsTotal()
		}
		else
		{
			0
		}
	}
	
	fun getRealContentItemsTotal(): Int
	{
		return super.getContentItemsTotal()
	}
	
	fun toggle()
	{
		val size = this.contentItemsTotal
		this._isExpanded = !this._isExpanded
		if (this._isExpanded)
		{
			this.adapter.notifyAllItemsInserted()
		}
		else
		{
			this.adapter.notifyItemRangeRemoved(0, size)
		}
	}
}