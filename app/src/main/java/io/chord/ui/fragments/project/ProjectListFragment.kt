package io.chord.ui.fragments.project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.clients.doOnSuccess
import io.chord.clients.models.Project
import io.chord.clients.observe
import io.chord.clients.toApiThrowable
import io.chord.services.managers.ProjectManager
import io.chord.ui.activities.SequencerActivity
import io.chord.ui.dialogs.cudc.CudcOperation
import io.chord.ui.dialogs.customs.SelectCudcOperationDialog
import io.chord.ui.dialogs.flows.ProjectFlow
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class ProjectListFragment : Fragment(), ClickListener<Project, ProjectViewHolder>
{
    private lateinit var flow: ProjectFlow
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SectionedRecyclerViewAdapter
    private lateinit var viewSection: Section<Project, ProjectViewHolder>
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
        this.viewSection = Section(
            R.layout.project_list_item,
            R.layout.project_list_header,
            { view -> ProjectViewHolder(view)},
            this
        )
    
        this.viewAdapter.addSection(this.viewSection)
        this.viewSection.setAdapter(this.viewAdapter)
    
        val view = LayoutInflater
            .from(this.context)
            .inflate(
                R.layout.project_list_fragment,
                container,
                false
            ) as RecyclerView
        
        this.recyclerView = view

        this.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addItemDecoration(DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            ))
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
        this.loadProjects()
    }

    override fun onItemClicked(item: Project, holder: ProjectViewHolder)
    {
        ProjectManager.setCurrent(item)
        this.startActivity(
            Intent(
                this.activity!!,
                SequencerActivity::class.java
            )
        )
    }

    override fun onItemLongClicked(item: Project, holder: ProjectViewHolder): Boolean
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
    
    override fun onFailedViewClicked(section: Section<Project, ProjectViewHolder>)
    {
        this.loadProjects()
    }
    
    fun create()
    {
        this.flow.create()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                this.viewSection.add(it)
            }
            .observe()
    }
    
    private fun update(project: Project)
    {
        this.flow.update(project)
            .doOnSuccess {
                this.viewSection.update(it)
            }
            .observe()
    }
    
    private fun delete(project: Project)
    {
        this.flow.delete(project)
            .doOnSuccess {
                this.viewSection.remove(it)
            }
            .observe()
    }
    
    private fun clone(project: Project)
    {
        this.flow.clone(project)
            .doOnSuccess {
                this.viewSection.add(it)
            }
            .observe()
    }
    
    private fun loadProjects()
    {
        this.flow.getAllByAuthor()
            .doOnSubscribe {
                this.viewSection.setStateLoading()
            }
            .doOnSuccess {
                this.viewSection.setDataset(it)
            }
            .doOnError { throwable ->
                throwable
                    .toApiThrowable()
                    .doOnError { code, _ ->
                        if(code == 404)
                        {
                            this.viewSection.setStateEmpty()
                        }
                        else
                        {
                            this.viewSection.setStateFailed()
                        }
                    }
                    .observe()
            }
            .observe()
    }
}