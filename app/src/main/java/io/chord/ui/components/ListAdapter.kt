package io.chord.ui.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import io.chord.ui.extensions.getDirectParentOfType
import kotlin.properties.Delegates

class ListAdapter<TModel, TViewModel: ListViewModel>(
	context: Context
) : ArrayAdapter<TModel>(
	context,
	0,
	mutableListOf()
)
{
	private val _items: MutableList<TModel> = mutableListOf()
	private val _holders: MutableList<ListViewHolder<TModel, TViewModel>> = mutableListOf()
	private val _recycledHolders: MutableList<ListViewHolder<TModel, TViewModel>> = mutableListOf()
	private var _notifyOnChange: Boolean = true
	
	var layoutId by Delegates.notNull<Int>()
	lateinit var listener: ListClickListener<TModel>
	lateinit var viewHolderFactory: ((View) -> ListViewHolder<TModel, TViewModel>)
	lateinit var viewModelFactory: ((TModel) -> TViewModel)
	
	val items: List<TModel>
		get() = this._items.toList()
	
	init
	{
		this.setNotifyOnChange(this._notifyOnChange)
	}
	
	override fun setNotifyOnChange(notifyOnChange: Boolean)
	{
		super.setNotifyOnChange(notifyOnChange)
		this._notifyOnChange = notifyOnChange
	}
	
	fun getNotifyOnChange(): Boolean
	{
		return this._notifyOnChange
	}
	
	fun recycleView(view: View)
	{
		val holder = this.getViewHolder(view)
		this.recycleView(holder)
	}
	
	private fun recycleView(holder: ListViewHolder<TModel, TViewModel>)
	{
		holder.unbind()
		val parent = holder.view.getDirectParentOfType<ViewGroup>()
		parent!!.removeView(holder.view)
		this._holders.remove(holder)
		this._recycledHolders.add(holder)
	}
	
	fun recycleViews()
	{
		this._holders.map {
			it.view
		}
		.forEach {
			this.recycleView(it)
		}
	}
	
	override fun add(item: TModel)
	{
		this._items.add(item)
		super.add(item)
	}
	
	override fun addAll(vararg items: TModel)
	{
		this._items.addAll(items.toList())
		super.addAll(*items)
	}
	
	override fun addAll(collection: MutableCollection<out TModel>)
	{
		this._items.addAll(collection)
		super.addAll(collection)
	}
	
	override fun insert(
		item: TModel,
		index: Int
	)
	{
		this._items.add(index, item)
		super.insert(item, index)
	}
	
	fun update(item: TModel)
	{
		val holder = this.getViewHolder(item)
		holder.model = item
		this.notifyDataSetInvalidated()
	}
	
	override fun remove(item: TModel)
	{
		val holder = this.getViewHolder(item)
		this.recycleView(holder)
		this._items.remove(item)
		super.remove(item)
	}
	
	override fun clear()
	{
		this._items.clear()
		super.clear()
	}
	
	fun move(from: Int, to: Int)
	{
		if(from == to)
		{
			return
		}
		
		val item = this._items[from]
		this._items.removeAt(from)
		this._items.add(to, item)
		super.clear()
		super.addAll(this._items)
	}
	
	private fun getViewHolder(item: TModel): ListViewHolder<TModel, TViewModel>
	{
		val result = this._holders
			.stream()
			.filter {
				it.model == item
			}
			.findFirst()
		
		if(!result.isPresent)
		{
			throw IllegalStateException("item does not exist")
		}
		
		return result.get()
	}
	
	fun getViewHolder(view: View): ListViewHolder<TModel, TViewModel>
	{
		val result = this._holders
			.stream()
			.filter {
				it.view == view
			}
			.findFirst()
		
		if(result.isPresent)
		{
			return result.get()
		}
		
		return this.viewHolderFactory(view)
	}
	
	fun bindViewHolder(holder: ListViewHolder<TModel, TViewModel>, position: Int)
	{
		val model = this.getItem(position)!!
		val viewModel = this.viewModelFactory(model)
		holder.bind(viewModel, this.listener)
	}
	
	override fun getView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		if(this._recycledHolders.size > 0)
		{
			val holder = this._recycledHolders.removeAt(0)
			this.bindViewHolder(holder, position)
			this._holders.add(holder)
			return holder.view
		}
		else
		{
			val view = LayoutInflater
				.from(this.context)
				.inflate(
					this.layoutId,
					parent,
					false
				)
			
			val holder = this.getViewHolder(view)
			this.bindViewHolder(holder, position)
			this._holders.add(holder)
			return view
		}
	}
}