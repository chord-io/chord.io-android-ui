package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import io.chord.R
import io.chord.ui.extensions.getParentRootView
import io.chord.ui.utils.QuantizeUtils
import kotlinx.android.synthetic.main.component_dropdown.view.*

class QuantizeDropDown : DropDown, AdapterView.OnItemSelectedListener, Binder
{
	private lateinit var adapter: QuantizeDropDownAdapter
	private val quantifiables: MutableMap<Int, Quantifiable> = mutableMapOf()
	private lateinit var selectedQuantization: QuantizeUtils.Quantization
	
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
		
		this.selectedQuantization = QuantizeUtils.Quantization(
			QuantizeUtils.QuantizeValue.First,
			QuantizeUtils.QuantizeMode.Natural
		)
		
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
		
		this.selectedQuantization = QuantizeUtils.Quantization(
			item.second, item.first
		)
		
		this.dispatchEvent()
	}
	
	override fun onNothingSelected(parent: AdapterView<*>?)
	{
		throw NotImplementedError()
	}
	
	override fun attach(id: Int)
	{
		val rootView = this.getParentRootView()
		val quantifiable = rootView.findViewById<View>(id)
		this.attach(quantifiable)
	}
	
	override fun attach(view: View)
	{
		this.quantifiables[id] = view as Quantifiable
		
		this.dispatchEvent()
	}
	
	override fun attachAll(views: List<View>)
	{
		views.forEach {
			this.quantifiables[it.id] = it as Quantifiable
		}
		
		this.dispatchEvent()
	}
	
	override fun detach(id: Int)
	{
		this.quantifiables.remove(id)
	}
	
	override fun detachAll()
	{
		this.quantifiables.clear()
	}
	
	private fun dispatchEvent()
	{
		this.quantifiables.forEach { (_, quantifiable) ->
			quantifiable.setQuantization(this.selectedQuantization)
		}
	}
}