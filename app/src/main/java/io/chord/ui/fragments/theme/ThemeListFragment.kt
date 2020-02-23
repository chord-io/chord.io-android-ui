package io.chord.ui.fragments.theme

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.clients.models.Project
import io.chord.clients.models.Theme
import io.chord.clients.models.Track
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.components.Listable
import io.chord.ui.dialogs.flows.ProjectFlow
import io.chord.ui.extensions.dpToPixel
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

class ThemeListFragment : Fragment(), ClickListener<Theme, ThemeViewHolder>, Listable<Track>
{
    private val bindableBehavior = BindableBehavior(this)
    private lateinit var flow: ProjectFlow
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SectionedRecyclerViewAdapter
    private lateinit var viewSection: Section<Theme, ThemeViewHolder>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        this.flow = ProjectFlow(this.activity!!)
        
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
    
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    )
    {
        super.onViewCreated(view, savedInstanceState)
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
//        val dialog = SelectCudcOperationDialog(
//            this.context as AppCompatActivity,
//            EnumSet.of(
//                CudcOperation.UPDATE,
//                CudcOperation.DELETE,
//                CudcOperation.CLONE
//            )
//        )
//
//        dialog.onDeleteSelected = { this.delete(item) }
//        dialog.onUpdateSelected = { this.update(item) }
//        dialog.onCloneSelected = { this.clone(item) }
//
//        dialog.show()
        return true
    }
    
    override fun onFailedViewClicked(section: Section<Theme, ThemeViewHolder>)
    {
    }
    
    fun create()
    {
//        this.flow.create()
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSuccess {
//                this.viewSection.add(it)
//            }
//            .observe()
    }
    
    private fun update(project: Project)
    {
//        this.flow.update(project)
//            .doOnSuccess {
//                this.viewSection.update(it)
//            }
//            .observe()
    }
    
    private fun delete(project: Project)
    {
//        this.flow.delete(project)
//            .doOnSuccess {
//                this.viewSection.remove(it)
//            }
//            .observe()
    }
    
    private fun clone(project: Project)
    {
//        this.flow.clone(project)
//            .doOnSuccess {
//                this.viewSection.add(it)
//            }
//            .observe()
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
        this.viewAdapter.removeAllSections()
        this.recyclerView.adapter = null
        this.recyclerView.layoutManager = null
        
        dataset.forEach {
            val section = Section(
                it.name,
                R.layout.theme_list_item,
                R.layout.theme_list_header,
                R.layout.section_mini_empty,
                R.layout.section_mini_loading,
                R.layout.section_mini_failed,
                { view -> ThemeViewHolder(view)},
                { view ->
                    val header = ThemeHeaderViewHolder(view)
                    val background = header.binding.layout.background as LayerDrawable
                    val drawable = background.getDrawable(0) as GradientDrawable
                    drawable.setStroke(2f.dpToPixel().toInt(), it.color)
                    header
                },
                this
            )
    
            this.viewAdapter.addSection(section)
            section.setAdapter(this.viewAdapter)
        }
        
        this.recyclerView.adapter = this.viewAdapter
        this.recyclerView.layoutManager = this.viewManager
        this.viewAdapter.notifyDataSetChanged()
    }
}