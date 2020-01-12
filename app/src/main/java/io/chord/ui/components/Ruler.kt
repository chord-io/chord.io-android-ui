package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Dimension
import io.chord.R
import io.chord.ui.utils.ViewUtils

class Ruler : View
{
	private val painter: Paint = Paint()
	
	private var _backgroundColor: Int? = null
	private var _defaultWidth: Float? = null
	private var _tickThickness: Float? = null
	
	var backgroundColor: Int?
		get() = this._backgroundColor
		set(value) {
			this._backgroundColor = value
		}
	
	var defaultWidth: Float?
		get() = this._defaultWidth
		set(value) {
			this._defaultWidth = value
		}
	
	var tickThickness: Float?
		get() = this._tickThickness
		set(value) {
			this._tickThickness = value
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
	
	private fun init(attrs: AttributeSet?, defStyle: Int)
	{
		val typedArray = this.context.obtainStyledAttributes(
			attrs, R.styleable.Ruler, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this.backgroundColor = typedArray.getColor(
			R.styleable.ScrollBar_cio_sb_backgroundColor,
			this.resources.getColor(R.color.backgroundSecondary, theme)
		)
		
		this.defaultWidth = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_defaultWidth,
			this.resources.getDimension(R.dimen.ruler_default_width)
		)
		
		this.tickThickness = typedArray.getDimension(
			R.styleable.Ruler_cio_rl_tickThickness,
			this.resources.getDimension(R.dimen.ruler_tick_thickness)
		)
		
		typedArray.recycle()
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		this.backgroundColor?.apply {
			painter.color = this
		}
		
		canvas?.drawRect(
			0f,
			0f,
			this.width.toFloat(),
			this.height.toFloat(),
			this.painter
		)
		
		this.drawBar(canvas, 0)
	}
	
	private fun drawBar(canvas: Canvas?, bar: Int)
	{
		val rect = RectF(
			0f,
			0f,
			this.defaultWidth!!,
			this.height.toFloat()
		)
		
		canvas?.drawRect(
			rect,
			this.painter
		)
	}
}