package io.chord.ui.section

import android.view.View
import android.widget.TabHost
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.chord.databinding.ProjectListItemBinding

abstract class ViewHolderBase<TItem, THolder: ViewHolderBase<TItem, THolder>>(
	itemView: View
) : RecyclerView.ViewHolder(itemView)
{
	abstract fun bind(item: TItem, clickListener: ClickListener<TItem, THolder>)
}