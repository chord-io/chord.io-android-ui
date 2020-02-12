package io.chord.ui.dialogs.sections

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.chord.R
import io.chord.ui.dialogs.BaseDialogFragment
import io.chord.ui.dialogs.DialogParameters
import io.chord.ui.extensions.getChildOfType
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.Section
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

class SectionDialogFragment(
    parameters: DialogParameters
) : BaseDialogFragment(parameters), ClickListener<SectionDialogItem, SectionDialogItemViewHolder>
{
    private lateinit var recyclerView: RecyclerView
    private val viewAdapter: SectionedRecyclerViewAdapter
    private val viewManager: RecyclerView.LayoutManager

    private lateinit var selectedItem: SectionDialogItem

    lateinit var onValidate: ((SectionDialogItem) -> Unit)

    init {
        this.viewManager = LinearLayoutManager(this.activity)
        this.viewAdapter = SectionedRecyclerViewAdapter()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        this.recyclerView = LayoutInflater
            .from(this.context)
            .inflate(
                R.layout.section_dialog_fragment,
                null,
                false
            ) as RecyclerView
        this.recyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
        this.recyclerView.itemAnimator = DefaultItemAnimator()
        
        val builder = this.getBuilder()
        builder.setView(this.recyclerView)
        builder.setPositiveButton(this.parameters.positiveButtonText) { _, _ -> run {
            this.onValidate(this.selectedItem)
        }}
        builder.setNegativeButton(this.parameters.negativeButtonText, null)
        return this.finalize(builder)
    }

    fun addSection(section: Section<SectionDialogItem, SectionDialogItemViewHolder>)
    {
        this.viewAdapter.addSection(section)
        section.setAdapter(this.viewAdapter)
    }

    fun removeSection(section: Section<SectionDialogItem, SectionDialogItemViewHolder>)
    {
        this.viewAdapter.removeSection(section)
    }

    override fun onItemClicked(item: SectionDialogItem, holder: SectionDialogItemViewHolder)
    {
        this.recyclerView.getChildOfType<RadioButton>()
            .forEach {
                it.isChecked = false
            }

        holder.binding.name.isChecked = true
        this.selectedItem = item
    }

    override fun onItemLongClicked(item: SectionDialogItem, holder: SectionDialogItemViewHolder): Boolean
    {
        return true
    }

    override fun onFailedViewClicked(section: Section<SectionDialogItem, SectionDialogItemViewHolder>)
    {

    }
}