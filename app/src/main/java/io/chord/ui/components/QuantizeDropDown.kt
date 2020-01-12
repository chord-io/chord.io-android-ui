package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import io.chord.R
import io.chord.ui.utils.QuantizeUtils
import kotlinx.android.synthetic.main.component_dropdown.view.*

class QuantizeDropDown : DropDown, AdapterView.OnItemSelectedListener
{
	private lateinit var adapter: QuantizeDropDownAdapter
	
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
		val text = this.adapter.getText(position, true)
		this.selectedItemTextView.text = text
	}
	
	override fun onNothingSelected(parent: AdapterView<*>?)
	{
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}