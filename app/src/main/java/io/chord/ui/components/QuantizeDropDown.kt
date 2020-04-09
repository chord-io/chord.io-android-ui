package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.chord.R
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Binder
import io.chord.ui.extensions.dpToPixel
import io.chord.ui.utils.QuantizeUtils
import kotlinx.android.synthetic.main.component_dropdown.view.*

class QuantizeDropDown : DropDown, AdapterView.OnItemSelectedListener, Binder
{
	private lateinit var adapter: QuantizeDropDownAdapter
	private val bindBehavior = BindBehavior<Quantifiable>(this)
	private lateinit var selectedQuantization: QuantizeUtils.Quantization
	
	constructor(context: Context?) : super(context)
	
	constructor(
		context: Context?,
		attrs: AttributeSet?
	) : super(context, attrs)
	{
		this.init(attrs, 0)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int
	) : super(context, attrs, defStyleAttr)
	{
		this.init(attrs, defStyleAttr)
	}
	
	constructor(
		context: Context?,
		attrs: AttributeSet?,
		defStyleAttr: Int,
		defStyleRes: Int
	) : super(context, attrs, defStyleAttr, defStyleRes)
	{
		this.init(attrs, defStyleAttr)
	}
	
	override fun init(attrs: AttributeSet?, defStyle: Int) {
		super.init(attrs, defStyle)
		
		this.selectedItemTextView.minWidth = 70f.dpToPixel().toInt()
		
		this.selectedQuantization = QuantizeUtils.Quantization(
			QuantizeUtils.QuantizeValue.Fourth,
			QuantizeUtils.QuantizeMode.Natural
		)
		
		this.adapter = QuantizeDropDownAdapter(this.context, R.layout.quantize_dropdown_item)
		this.dropdownView.onItemSelectedListener = this
		this.dropdownView.adapter = this.adapter
		this.dropdownView.setSelection(QuantizeUtils.QuantizeValue.Fourth.ordinal)
		
		this.bindBehavior.onAttach = {}
		this.bindBehavior.onDispatchEvent = {
			it.setQuantization(this.selectedQuantization)
		}
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
		
		this.bindBehavior.requestDispatchEvent()
	}
	
	override fun onNothingSelected(parent: AdapterView<*>?)
	{
		throw NotImplementedError()
	}
	
	override fun attach(id: Int)
	{
		this.bindBehavior.attach(id)
	}
	
	override fun attach(view: View)
	{
		this.bindBehavior.attach(view)
	}
	
	override fun attach(fragment: Fragment)
	{
		this.bindBehavior.attach(fragment)
	}
	
	override fun attach(activity: FragmentActivity)
	{
		this.bindBehavior.attach(activity)
	}
	
	override fun attachAll(views: List<View>)
	{
		this.bindBehavior.attachAll(views)
	}
	
	override fun detach(id: Int)
	{
		this.bindBehavior.detach(id)
	}
	
	override fun detach(view: View)
	{
		this.bindBehavior.detach(view)
	}
	
	override fun detachAll()
	{
		this.bindBehavior.detachAll()
	}
}