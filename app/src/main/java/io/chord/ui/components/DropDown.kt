package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginEnd
import androidx.core.view.setMargins
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import io.chord.R
import kotlinx.android.synthetic.main.component_dropdown.view.*

open class DropDown : LinearLayout
{
	private var _label: String? = null
	private var _textColor: Int? = null
	private var _iconSize: Int? = null
	private var _textSize: Int? = null
	
	var label: String?
		get() = this._label
		set(value) {
			this._label = value
			
			if(this._label == null)
			{
				this.labelView.visibility = View.GONE
			}
			else
			{
				this.labelView.visibility = View.VISIBLE
				this.labelView.text = value
			}
		}
	
	var textColor: Int?
		get() = this._textColor
		set(value) {
			this._textColor = value
			this._textColor?.apply {
				labelView.setTextColor(this)
				selectedItemTextView.setTextColor(this)
			}
			
			if(this._textColor == null)
			{
				val color = this.resources.getColor(R.color.textColor, this.context.theme)
				this.labelView.setTextColor(color)
				this.selectedItemTextView.setTextColor(color)
				this.iconView.setColorFilter(color)
				
			}
			else
			{
				this.labelView.setTextColor(value!!)
				this.selectedItemTextView.setTextColor(value)
				this.iconView.setColorFilter(value)
			}
		}
	
	var iconSize: Int?
		get() = this._iconSize
		set(value) {
			this._iconSize = value
			
			val dropDownLayoutParams = this.dropdownView.layoutParams as RelativeLayout.LayoutParams
			val iconLayoutParams = this.iconView.layoutParams as RelativeLayout.LayoutParams
			
			if(this._iconSize == null)
			{
				val size = this.resources.getDimension(R.dimen.dropdown_icon_size).toInt()
				dropDownLayoutParams.width = size
				dropDownLayoutParams.height = size
				iconLayoutParams.width = size
				iconLayoutParams.height = size
			}
			else
			{
				dropDownLayoutParams.width = value!!
				dropDownLayoutParams.height = value
				iconLayoutParams.width = value!!
				iconLayoutParams.height = value
			}
		}
	
	var textSize: Int?
		get() = this._textSize
		set(value) {
			this._textSize = value
			
			if(this._textSize == null)
			{
				val size = this.resources.getDimension(R.dimen.dropdown_text_size)
				this.labelView.textSize = size
				this.selectedItemTextView.textSize = size
			}
			else
			{
				val size = value!!.toFloat()
				this.labelView.textSize = size
				this.selectedItemTextView.textSize = size
			}
		}
	
	constructor(context: Context?) : super(context)
	{
		this.init(null, 0)
	}
	
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
	
	protected open fun init(attrs: AttributeSet?, defStyle: Int) {
		View.inflate(this.context, R.layout.component_dropdown, this)
		
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.DropDown, defStyle, 0
		)
		
		this.label = typedArray.getString(
			R.styleable.DropDown_cio_dd_label
		)
		
		this.textColor = typedArray.getColor(
			R.styleable.DropDown_cio_dd_textColor,
			R.color.textColor.toInt()
		)
		
		this.iconSize = typedArray.getDimension(
			R.styleable.DropDown_cio_dd_iconSize,
			this.resources.getDimension(R.dimen.dropdown_icon_size)
		).toInt()
		
		this.textSize = typedArray.getDimension(
			R.styleable.DropDown_cio_dd_textSize,
			this.resources.getDimension(R.dimen.dropdown_text_size)
		).toInt()
		
		typedArray.recycle()
	}
}