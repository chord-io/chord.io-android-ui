package io.chord.ui.fragments.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Theme
import io.chord.clients.models.Track
import io.chord.clients.observe
import io.chord.services.managers.ProjectManager
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.components.Listable
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.SelectCudcOperationDialog
import io.chord.ui.dialogs.flows.ThemeFlow
import io.chord.ui.sections.ExpandableSection
import io.chord.ui.sections.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.util.*

class ThemeListFragment : Fragment(), ThemeClickListener<Theme, ThemeViewHolder>, Listable<Track>
{
    private val bindableBehavior = BindableBehavior(this)
    private lateinit var flow: ThemeFlow
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SectionedRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    
    private fun getTracks(): List<Track>
    {
        return ProjectManager.getCurrent()!!.tracks
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        this.flow = ThemeFlow(this.activity!!)
        
        this.viewManager = LinearLayoutManager(this.activity)
        this.viewAdapter = SectionedRecyclerViewAdapter()
    
        val view = LayoutInflater
            .from(this.context)
            .inflate(
                R.layout.theme_list_fragment,
                container,
                false
            ) as RecyclerView
        
        this.recyclerView = view

        this.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        this.recyclerView.itemAnimator = DefaultItemAnimator()

        return view
    }

    override fun onItemClicked(item: Theme, holder: ThemeViewHolder)
    {
//        ProjectManager.setCurrent(item)
//        this.startActivity(
//            Intent(
//                this.activity!!,
//                SequencerActivity::class.java
//            )
//        )
    }

    override fun onItemLongClicked(item: Theme, holder: ThemeViewHolder): Boolean
    {
        val dialog = SelectCudcOperationDialog(
            this.context as AppCompatActivity,
            EnumSet.of(
                CudcOperation.UPDATE,
                CudcOperation.DELETE,
                CudcOperation.CLONE
            )
        )

        dialog.onDeleteSelected = { this.delete(item) }
        dialog.onUpdateSelected = { this.update(item) }
        dialog.onCloneSelected = { this.clone(item) }

        dialog.show()
        return true
    }
    
    override fun onFailedViewClicked(section: Section<Theme, ThemeViewHolder>)
    {
    }
    
    override fun onPlayClicked(item: Theme)
    {
        val count = this.recyclerView.childCount
        
        for(index in 0 until count)
        {
            val child = this.recyclerView.getChildAt(index)
            val holder = this.recyclerView.getChildViewHolder(child)
            
            if(holder is ThemeViewHolder)
            {
                holder.isPlaying = false
            }
        }
    }
    
    fun create()
    {
        this.flow.create()
            .doOnSuccess {
                this.setDataSet(this.getTracks())
            }
            .observe()
    }
    
    private fun update(item: Theme)
    {
        this.flow.update(item)
            .doOnSuccess {
                this.setDataSet(this.getTracks())
            }
            .observe()
    }
    
    private fun delete(item: Theme)
    {
        this.flow.delete(item)
            .doOnSuccess {
                this.setDataSet(this.getTracks())
            }
            .observe()
    }
    
    private fun clone(item: Theme)
    {
        this.flow.clone(item)
            .doOnSuccess {
                this.setDataSet(this.getTracks())
            }
            .observe()
    }
    
    override fun attach(controller: BindBehavior<Bindable>)
    {
        this.bindableBehavior.attach(controller)
    }
    
    override fun selfAttach()
    {
        this.bindableBehavior.selfAttach()
    }
    
    override fun selfDetach()
    {
        this.bindableBehavior.selfDetach()
    }
    
    override fun setDataSet(dataset: List<Track>)
    {
        val states = this.viewAdapter.copyOfSectionsMap.map {
            it.key to (it.value as ExpandableSection<*, *>).isExpanded
        }
        this.viewAdapter.removeAllSections()
        this.recyclerView.adapter = null
        this.recyclerView.layoutManager = null
    
        dataset.forEach { track ->
            val state = states.firstOrNull { it.first == track.name }
            val isExpanded = state?.second ?: false
            val themes = ProjectManager.getCurrent()!!.tracks
                .first {
                    it.name == track.name
                }
                .themes
            val section = ExpandableSection(
                track.name,
                R.layout.theme_list_item,
                R.layout.theme_list_header,
                R.layout.section_mini_empty,
                R.layout.section_mini_loading,
                R.layout.section_mini_failed,
                { view -> ThemeViewHolder(track.color, view) },
                { view -> ThemeHeaderViewHolder(track.color, isExpanded, view) },
                this
            )
    
            this.viewAdapter.addSection(track.name, section)
            section.setAdapter(this.viewAdapter)
            section.setDataset(themes)
            
            if(isExpanded)
            {
                section.isExpanded = states
                    .first {
                        it.first == track.name
                    }
                    .second
            }
            else
            {
                section.isExpanded = false
            }
        }
        
        this.recyclerView.adapter = this.viewAdapter
        this.recyclerView.layoutManager = this.viewManager
        this.viewAdapter.notifyDataSetChanged()
    }
}