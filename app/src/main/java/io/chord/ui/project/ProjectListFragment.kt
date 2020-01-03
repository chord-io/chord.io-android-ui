package io.chord.ui.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.warkiz.widget.IndicatorSeekBar
import io.chord.R
import io.chord.client.ClientApi
import io.chord.client.apis.ProjectsApi
import io.chord.client.models.Project
import io.chord.client.models.ProjectDto
import io.chord.ui.dialog.cudc.*
import io.github.luizgrp.sectionedrecyclerviewadapter.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.util.*

class ProjectListFragment : Fragment(), ClickListener
{
    private val client: ProjectsApi = ClientApi.getProjectsApi()
    private lateinit var dataset: MutableList<Project>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: SectionedRecyclerViewAdapter
    private lateinit var viewSection: ProjectListSection
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        this.dataset = mutableListOf(
            Project("", "project A", "midoriiro", 200, true, arrayListOf(), arrayListOf()),
            Project("", "project B", "midoriiro", 120, false, arrayListOf(), arrayListOf()),
            Project("", "project C", "midoriiro", 120, false, arrayListOf(), arrayListOf())
        )
        this.viewManager = LinearLayoutManager(this.activity)
        this.viewAdapter = SectionedRecyclerViewAdapter()
        this.viewSection = ProjectListSection(this)
		this.viewSection.setDataset(this.dataset)

        this.viewAdapter.addSection(viewSection)

        val view = inflater.inflate(R.layout.project_list_fragment, container, false) as RecyclerView
        this.recyclerView = view

        this.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        this.recyclerView.itemAnimator = DefaultItemAnimator()

        return view
    }

    override fun onItemClicked(project: Project)
    {
        Log.i("PROJECT", "item clicked")
    }

    override fun onItemLongClicked(project: Project): Boolean
    {
        val fragmentManager = this.activity !!.supportFragmentManager
        val dialogFragment =
            CudcSelectOperationDialogFragment(
                EnumSet.of(
                    CudcOperation.UPDATE,
                    CudcOperation.DELETE,
                    CudcOperation.CLONE
                )
            )
        
        dialogFragment.onDeleteSelectedListener = { this.client.delete(project.id) }
        dialogFragment.onUpdateSelectedListener = { this.showUpdateFormDialog(project) }
        // TODO: handle clone operation
    
        dialogFragment.show(fragmentManager, "fragment_project_cudc_dialog")
        return true
    }
    
    override fun onFailedViewClicked(section: Section)
    {
        Log.i("PROJECT", "failed view clicked")
    }
    
    private fun showUpdateFormDialog(project: Project)
    {
        val fragmentManager = this.activity!!.supportFragmentManager
        val dialogFragment =
            CudcFormOperationDialogFragment(
                CudcOperationInformation(
                    CudcOperation.UPDATE,
                    this.getString(R.string.project_entity_name)
                ),
                R.layout.project_dialog_form
            )
        
        dialogFragment.onLayoutCreatedListener = {
            val name = it!!.findViewById<EditText>(R.id.name)
            val author = it.findViewById<EditText>(R.id.author)
            val tempo = it.findViewById<IndicatorSeekBar>(R.id.tempo)
            val isPrivate = it.findViewById<Switch>(R.id.isPrivate)
            name.setText(project.name)
            author.setText(project.authorId)
            tempo.setProgress(project.tempo.toFloat())
            isPrivate.isChecked = project.isPrivate
        }
        
        dialogFragment.onLayoutUpdatedListener = { it ->
            val name = it!!.findViewById<EditText>(R.id.name)
            val author = it.findViewById<EditText>(R.id.author)
            val tempo = it.findViewById<IndicatorSeekBar>(R.id.tempo)
            val isPrivate = it.findViewById<Switch>(R.id.isPrivate)
            val projectToUpdate = project.copy()
            projectToUpdate.name = name.text.toString()
            projectToUpdate.authorId = author.text.toString()
            projectToUpdate.tempo = tempo.progress
            projectToUpdate.isPrivate = isPrivate.isChecked
            
            this.client.update(project.id, ProjectDto(
                projectToUpdate.name,
                projectToUpdate.authorId,
                projectToUpdate.tempo,
                projectToUpdate.isPrivate,
                projectToUpdate.tracks,
                projectToUpdate.themes
            )).subscribe({
                val index = this.dataset.indexOfFirst { it.id == projectToUpdate.id }
                this.dataset[index] = projectToUpdate
                this.viewAdapter.notifyDataSetChanged()
            }, { error ->
                error.printStackTrace() // TODO: handle error
            })
        }
    
        dialogFragment.show(fragmentManager, "fragment_project_update_form")
    }
}