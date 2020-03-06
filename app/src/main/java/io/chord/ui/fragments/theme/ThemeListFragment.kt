package io.chord.ui.fragments.theme

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Theme
import io.chord.clients.models.Track
import io.chord.clients.observe
import io.chord.services.managers.ProjectManager
import io.chord.ui.activities.EditorActivity
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.components.Listable
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.SelectCudcOperationDialog
import io.chord.ui.dialogs.flows.ThemeFlow
import io.chord.ui.extensions.toHexaDecimalString
import io.chord.ui.sections.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator
import java.util.*

class ThemeListFragment : Fragment(), ThemeClickListener, Listable<Track>
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
            this.layoutManager = viewManager
            this.adapter = viewAdapter
            this.itemAnimator = SlideInRightAnimator(FastOutSlowInInterpolator())
        }

        return view
    }

    override fun onItemClicked(item: ThemeSectionItem, holder: ThemeViewHolder)
    {
        val intent = Intent(
            this.activity!!,
            EditorActivity::class.java
        )
        
        intent.putExtra("track", item.track.name)
        intent.putExtra("theme", item.theme.name)
        
        this.startActivity(intent)
    }

    override fun onItemLongClicked(item: ThemeSectionItem, holder: ThemeViewHolder): Boolean
    {
        val dialog = SelectCudcOperationDialog(
            this.context as AppCompatActivity,
            EnumSet.of(
                CudcOperation.UPDATE,
                CudcOperation.DELETE,
                CudcOperation.CLONE
            )
        )

        dialog.onDeleteSelected = { this.delete(item.theme) }
        dialog.onUpdateSelected = { this.update(item.theme) }
        dialog.onCloneSelected = { this.clone(item.theme) }

        dialog.show()
        return true
    }
    
    override fun onFailedViewClicked(section: Section<ThemeSectionItem, ThemeViewHolder>)
    {
    }
    
    override fun onPlayClicked(item: ThemeSectionItem)
    {
        // TODO play theme with midi engine
    }
    
    override fun onStopClicked(item: ThemeSectionItem)
    {
        // TODO play theme with midi engine
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
        // TODO: stop playing theme when dataset changed
        
        val sections = this.viewAdapter.copyOfSectionsMap
        val referenceIds = dataset.map {
            it.referenceId.toHexaDecimalString()
        }
    
        sections.filterKeys {
            !referenceIds.contains(it)
        }
        .forEach {
            val index = sections.keys.indexOf(it.key)
            sections.remove(it.key)
            this.viewAdapter.notifyItemRemoved(index)
        }
        
        dataset.forEach { track ->
            val referenceId = track.referenceId.toHexaDecimalString()
            val themes = track.themes
            val sectionIndex = sections.entries.indexOfFirst {
                it.key == referenceId
            }
            
            if(sectionIndex == -1)
            {
                val section = ThemeSection(
                    track,
                    false,
                    this
                )
    
                this.viewAdapter.addSection(referenceId, section)
                section.setAdapter(this.viewAdapter)
                section.setDataset(themes.map {
                    ThemeSectionItem(dataset, track, it)
                })
            }
            else
            {
                val trackIndex = dataset.indexOf(track)
                val section = this.viewAdapter.getSection(referenceId) as ThemeSection
                section.track = track
                section.setDataset(themes.map {
                    ThemeSectionItem(dataset, track, it)
                })
                
                if(sectionIndex != trackIndex)
                {
                    this.viewAdapter.removeSection(referenceId)
                    this.viewAdapter.addSection(referenceId, section)
                    this.viewAdapter.notifyItemMoved(sectionIndex, trackIndex)
                }
                else
                {
                    this.viewAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}