package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import io.chord.R
import kotlinx.android.synthetic.main.component_dropdown.view.*

abstract class DropDown : LinearLayout
{
	private var _label: String? = null
	private var _textColor: Int = 0
	private var _iconSize: Int = 0
	private var _textSize: Int = 0
	
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
	
	var textColor: Int
		get() = this._textColor
		set(value) {
			this._textColor = value.apply {
				labelView.setTextColor(this)
				selectedItemTextView.setTextColor(this)
			}
			
			this.labelView.setTextColor(value)
			this.selectedItemTextView.setTextColor(value)
			this.iconView.setColorFilter(value)
		}
	
	var iconSize: Int
		get() = this._iconSize
		set(value) {
			this._iconSize = value.apply {
				val dropDownLayoutParams = dropdownView.layoutParams as RelativeLayout.LayoutParams
				val iconLayoutParams = iconView.layoutParams as RelativeLayout.LayoutParams
				dropDownLayoutParams.width = this
				dropDownLayoutParams.height = this
				iconLayoutParams.width = this
				iconLayoutParams.height = this
			}
		}
	
	var textSize: Int
		get() = this._textSize
		set(value) {
			this._textSize = value.apply {
				val size = this.toFloat()
				labelView.textSize = size
				selectedItemTextView.textSize = size
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
			this.resources.getColor(R.color.textColor, this.context.theme)
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
		
		val layout = this.findViewById<LinearLayout>(R.id.layout)
		val dropdown = this.findViewById<Spinner>(R.id.dropdownView)
		layout.setOnClickListener {
			dropdown.performClick()
		}
	}
}