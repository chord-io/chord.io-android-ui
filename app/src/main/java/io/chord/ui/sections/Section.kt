package io.chord.ui.sections

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

open class Section<TItem, THolder: ViewHolderBase<TItem, THolder>>: Section
{
	private var title: String? = null
	private val dataset: MutableList<TItem> = mutableListOf()
	protected lateinit var adapter: SectionAdapter
	private val holderFactory: ((view: View) -> THolder)
	private var headerHolderFactory: ((view: View) -> HeaderViewHolder<TItem, THolder>)? = null
	private val clickListener: ClickListener<TItem, THolder>
	
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
		SectionParameters
			.builder()
			.itemResourceId(itemResourceId)
			.headerResourceId(headerResourceId)
			.loadingResourceId(loadingResourceId)
			.failedResourceId(failedResourceId)
			.emptyResourceId(emptyResourceId)
			.build()
	)
	{
		this.state = State.EMPTY
		this.title = title
		this.holderFactory = holderFactory
		this.headerHolderFactory = headerHolderFactory
		this.clickListener = clickListener
	}
	
	constructor(
		itemResourceId: Int,
		headerResourceId: Int,
		holderFactory: (view: View) -> THolder,
		clickListener: ClickListener<TItem, THolder>
	) : super(
		SectionParameters
			.builder()
			.itemResourceId(itemResourceId)
			.headerResourceId(headerResourceId)
			.loadingResourceId(R.layout.section_loading)
			.failedResourceId(R.layout.section_failed)
			.emptyResourceId(R.layout.section_empty)
			.build()
	)
	{
		this.state = State.EMPTY
		this.holderFactory = holderFactory
		this.clickListener = clickListener
	}
	
	constructor(
		itemResourceId: Int,
		holderFactory: (view: View) -> THolder,
		clickListener: ClickListener<TItem, THolder>
	) : super(
		SectionParameters
			.builder()
			.itemResourceId(itemResourceId)
			.loadingResourceId(R.layout.section_loading)
			.failedResourceId(R.layout.section_failed)
			.emptyResourceId(R.layout.section_empty)
			.build()
	)
	{
		this.state = State.EMPTY
		this.holderFactory = holderFactory
		this.clickListener = clickListener
	}
	
	fun changeState(state: State)
	{
		val previousState = this.state
		this.state = state
		
		when
		{
			state == previousState -> return
			state != State.LOADED ->
			{
				this.adapter.notifyStateChangedFromLoaded(this.contentItemsTotal)
			}
			state == State.LOADED ->
			{
				this.adapter.notifyStateChangedToLoaded(previousState)
			}
			else ->
			{
				this.adapter.notifyNotLoadedStateChanged(previousState)
			}
		}
	}
	
	fun setStateLoading()
	{
		this.changeState(State.LOADING)
	}
	
	fun setStateLoaded()
	{
		this.changeState(State.LOADED)
	}
	
	fun setStateEmpty()
	{
		this.changeState(State.EMPTY)
	}
	
	fun setStateFailed()
	{
		this.changeState(State.FAILED)
	}

	fun getDataSet(): List<TItem>
	{
		return this.dataset.toList()
	}
	
	fun setDataset(dataset: List<TItem>)
	{
		if(this.dataset == dataset)
		{
			return
		}
		
		this.dataset.clear()
		this.dataset.addAll(dataset)
		
		if(this.dataset.size == 0)
		{
			this.setStateEmpty()
		}
		else
		{
			this.setStateLoaded()
		}
		
		this.adapter.notifyAllItemsChanged()
	}
	
	fun setAdapter(adapter: SectionedRecyclerViewAdapter)
	{
		this.adapter = adapter.getAdapterForSection(this)
	}
	
	fun add(item: TItem)
	{
		this.dataset.add(item)
		
		if(this.state == State.EMPTY)
		{
			this.setStateLoaded()
		}
		
		this.adapter.notifyItemInserted(this.contentItemsTotal)
	}
	
	fun update(item: TItem)
	{
		val index = this.dataset.indexOf(item)
		this.dataset[index] = item
		this.adapter.notifyItemChanged(index)
	}
	
	fun remove(item: TItem)
	{
		val index = this.dataset.indexOf(item)
		this.dataset.remove(item)
		
		this.adapter.notifyItemRemoved(index)
		
		if(this.contentItemsTotal == 0)
		{
			this.setStateEmpty()
		}
	}
	
	override fun getContentItemsTotal(): Int
	{
		return this.dataset.size
	}
	
	override fun getItemViewHolder(view: View): RecyclerView.ViewHolder
	{
		return this.holderFactory(view)
	}
	
	override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder
	{
		return if(this.title == null) EmptyViewHolder(view) else this.headerHolderFactory!!(view)
	}
	
	override fun getLoadingViewHolder(view: View): RecyclerView.ViewHolder
	{
		return EmptyViewHolder(view)
	}
	
	override fun getFailedViewHolder(view: View): RecyclerView.ViewHolder
	{
		return FailedViewHolder<TItem, THolder>(view)
	}
	
	override fun getEmptyViewHolder(view: View): RecyclerView.ViewHolder
	{
		return EmptyViewHolder(view)
	}
	
	@Suppress("UNCHECKED_CAST")
	override fun onBindItemViewHolder(
		holder: RecyclerView.ViewHolder,
		position: Int
	)
	{
		(holder as? ViewHolderBase<TItem, THolder>)?.bind(
			this.dataset[position],
			this.clickListener
		)
	}
	
	@Suppress("UNCHECKED_CAST")
	override fun onBindFailedViewHolder(holder: RecyclerView.ViewHolder?)
	{
		(holder as? FailedViewHolder<TItem, THolder>)?.bind(
			this,
			this.clickListener
		)
	}
	
	@Suppress("UNCHECKED_CAST")
	override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?)
	{
		(holder as? HeaderViewHolder<TItem, THolder>)?.bind(
			this.title!!,
			this,
			this.clickListener
		)
	}
}