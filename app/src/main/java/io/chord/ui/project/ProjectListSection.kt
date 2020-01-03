package io.chord.ui.project

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.client.models.Project
import io.chord.ui.utils.FailedViewHolder
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.utils.EmptyViewHolder

class ProjectListSection(
	private var clickListener: ClickListener
) : Section(
	SectionParameters
		.builder()
		.itemResourceId(R.layout.project_list_item)
		.headerResourceId(R.layout.project_list_header)
		.loadingResourceId(R.layout.section_loading)
		.failedResourceId(R.layout.section_failed)
		.build()
)
{
	private var dataset: MutableList<Project> = mutableListOf<Project>()
	
	fun setDataset(dataset: List<Project>)
	{
		this.dataset.clear()
		this.dataset.addAll(dataset)
	}
	
	override fun getContentItemsTotal(): Int
	{
		return this.dataset.size
	}
	
	override fun getItemViewHolder(view: View): RecyclerView.ViewHolder
	{
		return ProjectViewHolder(view)
	}
	
	override fun getFailedViewHolder(view: View): RecyclerView.ViewHolder
	{
		return FailedViewHolder(view)
	}
	
	override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder
	{
		return EmptyViewHolder(view)
	}
	
	override fun getLoadingViewHolder(view: View?): RecyclerView.ViewHolder
	{
		return EmptyViewHolder(view)
	}
	
	override fun onBindItemViewHolder(
		holder: RecyclerView.ViewHolder?,
		position: Int
	)
	{
		val itemHolder = holder as ProjectViewHolder
		itemHolder.bind(this.dataset[position], this.clickListener)
	}
	
	override fun onBindFailedViewHolder(holder: RecyclerView.ViewHolder?)
	{
		val failedViewHolder = holder as FailedViewHolder
		failedViewHolder.bind(this, this.clickListener)
	}
}