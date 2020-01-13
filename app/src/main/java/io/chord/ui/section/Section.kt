package io.chord.ui.section

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.client.models.Project
import io.chord.ui.project.ProjectViewHolder
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder
import java.util.concurrent.TimeUnit

class Section<TItem, THolder: ViewHolderBase<TItem, THolder>>(
	itemResourceId: Int,
	headerResourceId: Int,
	private val holderFactory: (view: View) -> THolder,
	private val clickListener: ClickListener<TItem, THolder>
) : Section(
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
	private val dataset: MutableList<TItem> = mutableListOf()
	private lateinit var adapter: SectionAdapter
	
	fun changeState(state: State)
	{
		val previousState = this.state
		this.state = state
		
		when
		{
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
	
	fun setDataset(dataset: List<TItem>)
	{
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
	}
	
	fun setAdapter(adapter: SectionedRecyclerViewAdapter)
	{
		this.adapter = adapter.getAdapterForSection(this)
		this.setStateLoading()
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
		return EmptyViewHolder(view)
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
}