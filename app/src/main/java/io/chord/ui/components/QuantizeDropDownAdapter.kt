package io.chord.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.chord.R
import io.chord.ui.utils.QuantizeUtils
import org.w3c.dom.Text

class QuantizeDropDownAdapter(
	context: Context,
	resource: Int
) : ArrayAdapter<Pair<QuantizeUtils.QuantizeMode, QuantizeUtils.QuantizeValue>>(
	context,
	resource,
	QuantizeUtils.values
)
{
	fun getText(position: Int, short: Boolean): String
	{
		val item = this.getItem(position)!!
		val mode = when(item.first)
		{
			QuantizeUtils.QuantizeMode.Ternary -> if(short)
			{
				this.context.resources.getString(R.string.quanize_mode_ternary_short)
			}
			else
			{
				this.context.resources.getString(R.string.quanize_mode_ternary)
			}
			QuantizeUtils.QuantizeMode.Dotted -> if(short)
			{
				this.context.resources.getString(R.string.quanize_mode_dotted_short)
			}
			else
			{
				this.context.resources.getString(R.string.quanize_mode_dotted)
			}
			else -> ""
		}
		
		return "1/${item.second.value} $mode"
	}
	
	override fun getView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		val view = super.getView(position, convertView, parent) as TextView
		view.text = this.getText(position, true)
		return view
	}
	
	override fun getDropDownView(
		position: Int,
		convertView: View?,
		parent: ViewGroup
	): View
	{
		val view = super.getDropDownView(position, convertView, parent) as TextView
		view.text = this.getText(position, false)
		return view
	}
}