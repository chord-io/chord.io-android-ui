package io.chord.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.clients.ClientApi
import io.chord.clients.apis.ProjectsApi
import io.chord.clients.models.Project
import io.chord.clients.toApiThrowable
import io.chord.databinding.ProjectDialogFormBinding
import io.chord.services.managers.Manager
import io.chord.ui.ChordIOApplication
import io.chord.ui.activities.SequencerActivity
import io.chord.ui.dialogs.cudc.*
import io.chord.ui.extensions.observe
import io.chord.ui.extensions.toBanerApiThrowable
import io.chord.ui.models.ProjectDialogFormViewModel
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*

class ProjectListFragment : Fragment(), ClickListener<Project, ProjectViewHolder>
{
    private val client: ProjectsApi = ClientApi.getProjectsApi()
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

        val view = inflater.inflate(R.layout.project_list_fragment, container, false) as RecyclerView
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
        Manager.setCurrent(item)
        this.startActivity(
            Intent(
                this.activity!!,
                SequencerActivity::class.java
            )
        )
    }

    override fun onItemLongClicked(item: Project, holder: ProjectViewHolder): Boolean
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
        
        dialogFragment.onDeleteSelectedListener = { this.delete(item, holder) }
        dialogFragment.onUpdateSelectedListener = { this.update(item) }
        dialogFragment.onCloneSelectedListener = { this.clone(item) }
    
        dialogFragment.show(fragmentManager, "fragment_project_cudc_dialog")
        return true
    }
    
    override fun onFailedViewClicked(section: Section<Project, ProjectViewHolder>)
    {
        this.loadProjects()
    }
    
    fun create()
    {
        val fragmentManager = this.activity!!.supportFragmentManager
        val dialogFragment = CudcFormOperationDialogFragment<ProjectDialogFormBinding>(
            CudcOperationInformation(
                CudcOperation.CREATE,
                this.getString(R.string.project_entity_name)
            ),
            R.layout.project_dialog_form
        )
        
        dialogFragment.onViewModelBinding = {
            it.application = ChordIOApplication.instance
            it.project = ProjectDialogFormViewModel(null)
        }
        
        dialogFragment.onLayoutUpdatedListener = { binding: ProjectDialogFormBinding ->
            val projectToCreate = binding.project!!.toDto()
        
            this.client.create(projectToCreate)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    dialogFragment.banner.dismiss()
                    binding.nameLayout.isErrorEnabled = false
                    binding.authorLayout.isErrorEnabled = false
                    binding.author.isEnabled = false
                }
                .doOnSuccess { project ->
                    this.viewSection.add(project)
                    dialogFragment.validate()
                }
                .doOnError { throwable ->
                    throwable
                        .toBanerApiThrowable(dialogFragment.banner)
                        .doOnValidationError { mapper ->
                            mapper
                                .map("Name") { error ->
                                    binding.nameLayout.isErrorEnabled = true
                                    binding.nameLayout.error = error
                                }
                                .map("AuthorId") { error ->
                                    binding.authorLayout.isErrorEnabled = true
                                    binding.authorLayout.error = error
                                    binding.author.isEnabled = false
                                }
                                .observe()
                        }
                        .doOnPostObservation {
                            dialogFragment.unvalidate()
                            binding.author.isEnabled = false
                        }
                        .observe()
                }
                .observe()
        }
    
        dialogFragment.show(fragmentManager, "fragment_project_create_form")
    }
    
    private fun update(project: Project)
    {
        val fragmentManager = this.activity!!.supportFragmentManager
        val dialogFragment =
            CudcFormOperationDialogFragment<ProjectDialogFormBinding>(
                CudcOperationInformation(
                    CudcOperation.UPDATE,
                    this.getString(R.string.project_entity_name)
                ),
                R.layout.project_dialog_form
            )
    
        dialogFragment.onViewModelBinding = {
            it.application = ChordIOApplication.instance
            it.project = ProjectDialogFormViewModel(project)
        }

        dialogFragment.onLayoutUpdatedListener = { binding: ProjectDialogFormBinding ->
            val projectToUpdate = binding.project!!.toDto()

            this.client.update(project.id, projectToUpdate)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    dialogFragment.banner.dismiss()
                    binding.nameLayout.isErrorEnabled = false
                    binding.authorLayout.isErrorEnabled = false
                    binding.author.isEnabled = false
                }
                .doOnComplete {
                    project.name = projectToUpdate.name
                    project.tempo = projectToUpdate.tempo
                    project.isPrivate = projectToUpdate.isPrivate
                    this.viewSection.update(project)
                    dialogFragment.validate()
                }
                .doOnError { throwable ->
                    throwable
                        .toBanerApiThrowable(dialogFragment.banner)
                        .doOnValidationError { mapper ->
                            mapper
                                .map("Name") { error ->
                                    binding.nameLayout.isErrorEnabled = true
                                    binding.nameLayout.error = error
                                }
                                .map("AuthorId") { error ->
                                    binding.authorLayout.isErrorEnabled = true
                                    binding.authorLayout.error = error
                                    binding.author.isEnabled = false
                                }
                                .observe()
                        }
                        .doOnPostObservation {
                            dialogFragment.unvalidate()
                            binding.author.isEnabled = false
                        }
                        .observe()
                }
                .observe()
        }

        dialogFragment.show(fragmentManager, "fragment_project_update_form")
    }
    
    private fun delete(project: Project, holder: ProjectViewHolder)
    {
        this.client.delete(project.id)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                holder.binding!!.loader.visibility = View.VISIBLE
            }
            .doOnComplete {
                this.viewSection.remove(project)
            }
            .doOnError {
                // TODO: do something on error
            }
            .observe()
    }
    
    private fun clone(project: Project)
    {
        val fragmentManager = this.activity!!.supportFragmentManager
        val dialogFragment = CudcFormOperationDialogFragment<ProjectDialogFormBinding>(
            CudcOperationInformation(
                CudcOperation.CLONE,
                this.getString(R.string.project_entity_name)
            ),
            R.layout.project_dialog_form
        )
        
        dialogFragment.onViewModelBinding = {
            it.application = ChordIOApplication.instance
            it.project = ProjectDialogFormViewModel(project.copy())
        }
        
        dialogFragment.onLayoutUpdatedListener = { binding: ProjectDialogFormBinding ->
            val projectToCreate = binding.project!!.toDto()
            
            this.client.create(projectToCreate)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    dialogFragment.banner.dismiss()
                    binding.nameLayout.isErrorEnabled = false
                    binding.authorLayout.isErrorEnabled = false
                    binding.author.isEnabled = false
                }
                .doOnSuccess { project ->
                    this.viewSection.add(project)
                    dialogFragment.validate()
                }
                .doOnError { throwable ->
                    throwable
                        .toBanerApiThrowable(dialogFragment.banner)
                        .doOnValidationError { mapper ->
                            mapper
                                .map("Name") { error ->
                                    binding.nameLayout.isErrorEnabled = true
                                    binding.nameLayout.error = error
                                }
                                .map("AuthorId") { error ->
                                    binding.authorLayout.isErrorEnabled = true
                                    binding.authorLayout.error = error
                                    binding.author.isEnabled = false
                                }
                                .observe()
                        }
                        .doOnPostObservation {
                            dialogFragment.unvalidate()
                            binding.author.isEnabled = false
                        }
                        .observe()
                }
                .observe()
        }
        
        dialogFragment.show(fragmentManager, "fragment_project_create_form")
    }
    
    private fun loadProjects()
    {
        this.client.getAllByAuthor()
            .observeOn(AndroidSchedulers.mainThread())
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