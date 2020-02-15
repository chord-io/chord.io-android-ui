package io.chord.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.GridLayout
import io.chord.R

class ColorPicker : GridLayout
{
	private class Button(
		private val context: Context,
		private val layout: ColorPicker,
		private val color: Int
	)
	{
		val view: View = View(this.context)
		
		init
		{
			this.view.isClickable = true
			this.view.isFocusable = true
			this.view.background = this.generateDrawable(color)
			
			this.view.setOnClickListener { buttonClicked ->
				this.layout.buttons.forEach {
					it.view.background = this.generateDrawable(it.color)
				}
				buttonClicked.background = this.generateCheckedDrawable(color)
				this.layout._selectedColor = color
			}
		}
		
		private fun generateDrawable(color: Int): Drawable
		{
			val drawable = this.context.resources.getDrawable(
				R.drawable.color_picker_background,
				context.theme
			) as GradientDrawable
			drawable.setColor(color)
			return drawable
		}
		
		private fun generateCheckedDrawable(color: Int): Drawable
		{
			val drawable = this.context.resources.getDrawable(
				R.drawable.color_picker_background_checked,
				context.theme
			) as GradientDrawable
			drawable.setColor(color)
			return drawable
		}
	}
	
	private val buttons: MutableList<Button> = mutableListOf()
	
	private var _selectedColor: Int = -1
	private var colors: MutableList<Int> = mutableListOf()
	
	val selectedColor: Int
		get() = this._selectedColor
	
	private var _defaultColor: Int = -1
	
	var defaultColor: Int
		get() = this._defaultColor
		set(value) {
			this._defaultColor = value
			this.generateLayout()
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
	
	private fun init(attrs: AttributeSet?, defStyle: Int) {
		View.inflate(this.context, R.layout.component_color_picker, this)
		
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.ColorPicker, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._defaultColor = typedArray.getColor(
			R.styleable.ColorPicker_cio_cp_defaultColor,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		typedArray.recycle()
		
		this.generateLayout()
	}
	
	private fun getColors(): List<Int>
	{
		if(this.colors.isNotEmpty())
		{
			return this.colors
		}
		
		val theme = this.context.theme
		
		for(index in 1..16)
		{
			val id = this.resources.getIdentifier(
				"rainbow$index",
				"color",
				this.context.packageName
			)
			
			this.colors.add(this.resources.getColor(id, theme))
		}
		
		return this.colors
	}
	
	private fun generateLayout()
	{
		this.removeAllViews()
		this.buttons.clear()
		this.columnCount = 9
		this.rowCount = 2
		this.useDefaultMargins = true
		
		val columnWeight = 1f / 9f
		val colors = this.getColors()
		
		for(index in colors.indices)
		{
			val color = colors[index]
			val row = if(index < 8) 0 else 1
			val column = if(index < 8) index else index - 8
			this.addView(
				GridLayout.spec(column + 1, columnWeight),
				GridLayout.spec(row, 1f),
				color
			)
		}
		
		this.addDefaultView(
			GridLayout.spec(0, columnWeight),
			GridLayout.spec(0, 0),
			this.defaultColor
		)
	}
	
	private fun generateButton(color: Int): View
	{
		val button = Button(
			this.context,
			this,
			color
		)
		this.buttons.add(button)
		return button.view
	}
	
	private fun addView(
		layoutParameters: GridLayout.LayoutParams,
		columnSpec: Spec,
		rowSpec: Spec,
		color: Int
	)
	{
		layoutParameters.columnSpec = columnSpec
		layoutParameters.rowSpec = rowSpec
		val view = this.generateButton(color)
		view.layoutParams = layoutParameters
		this.addView(view)
	}
	
	private fun addView(columnSpec: Spec, rowSpec: Spec, color: Int)
	{
		val layoutParameters = GridLayout.LayoutParams()
		layoutParameters.width = 0
		layoutParameters.height = 0
		layoutParameters.columnSpec = columnSpec
		layoutParameters.rowSpec = rowSpec
		this.addView(layoutParameters, columnSpec, rowSpec, color)
	}
	
	private fun addDefaultView(columnSpec: Spec, rowSpec: Spec, color: Int)
	{
		val layoutParameters = GridLayout.LayoutParams()
		layoutParameters.width = 0
		layoutParameters.height = LayoutParams.MATCH_PARENT
		layoutParameters.columnSpec = columnSpec
		layoutParameters.rowSpec = rowSpec
		this.addView(layoutParameters, columnSpec, rowSpec, color)
	}
}