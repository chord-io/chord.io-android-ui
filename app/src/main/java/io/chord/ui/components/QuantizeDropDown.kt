package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import io.chord.R
import io.chord.ui.utils.QuantizeUtils
import io.chord.ui.utils.ViewUtils
import kotlinx.android.synthetic.main.component_dropdown.view.*

class QuantizeDropDown : DropDown, AdapterView.OnItemSelectedListener, Binder
{
	private lateinit var adapter: QuantizeDropDownAdapter
	private val quantifiables: MutableMap<Int, Quantifiable> = mutableMapOf()
	
	constructor(context: Context?) : super(context)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?
	) : super(context, attrs)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	
	override fun init(attrs: AttributeSet?, defStyle: Int) {
		super.init(attrs, defStyle)
		
		this.adapter = QuantizeDropDownAdapter(this.context, R.layout.quantize_dropdown_item)
		this.dropdownView.setSelection(0)
		this.dropdownView.onItemSelectedListener = this
		this.dropdownView.adapter = this.adapter
	}
	
	override fun onItemSelected(
		parent: AdapterView<*>?,
		view: View?,
		position: Int,
		id: Long
	)
	{
		val item = this.adapter.getItem(position)!!
		val text = this.adapter.getText(position, true)
		this.selectedItemTextView.text = text
		
		val quantization = QuantizeUtils.Quantization(
			item.second, item.first
		)
		
		this.quantifiables.forEach { (_, quantifiable) ->
			quantifiable.setQuantization(quantization)
		}
	}
	
	override fun onNothingSelected(parent: AdapterView<*>?)
	{
		throw NotImplementedError()
	}
	
	override fun attach(id: Int)
	{
		val rootView = ViewUtils.getParentRootView(this)
		val quantifiable = rootView.findViewById<View>(id)
		this.quantifiables[id] = quantifiable as Quantifiable
	}
	
	override fun detach(id: Int)
	{
		this.quantifiables.remove(id)
	}
}