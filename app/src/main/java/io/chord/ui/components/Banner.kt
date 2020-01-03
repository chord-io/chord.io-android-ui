package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import io.chord.R
import kotlinx.android.synthetic.main.banner.view.*

class Banner : LinearLayout
{
	private var _message: String? = null
	private var _icon: String? = null
	private var _backgroundColor: Int? = null
	private var _color: Int? = null
	
	var message: String?
		get() = this._message
		set(value) {
			this._message = value
			this.messageView.text = value
		}
	
	@Suppress("SetterBackingFieldAssignment")
	var messageId: Int? = null
		set(value) {
			this._message = value?.let { this.context.getString(it) }
		}
	
	var icon: String?
		get() = this._icon
		set(value) {
			this._icon = value
			
			if(value != null)
			{
				this.iconView.visibility = View.VISIBLE
				this.iconView.icon = IconicsDrawable(this.context)
					.icon(value)
				this.updateColor()
			}
			else
			{
				this.iconView.visibility = View.GONE
			}
		}
	
	var backgroundColor: Int?
		get() = this._backgroundColor
		set(value) {
			this._backgroundColor = value
			this.updateColor()
		}
	
	var color: Int?
		get() = this._color
		set(value) {
			this._color = value
			this.updateColor()
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
	
	private fun init(attrs: AttributeSet?, defStyle: Int) {
		// Load attributes
		View.inflate(context, R.layout.banner, this)
		
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.Banner, defStyle, 0
		)
		
		this.message = typedArray.getString(
			R.styleable.Banner_cio_message
		)
		
		this.icon = typedArray.getString(
			R.styleable.Banner_cio_icon
		)
		
		this.backgroundColor = typedArray.getColor(
			R.styleable.Banner_cio_backgroundColor,
			R.color.backgroundSecondary.toInt()
		)
		
		this.color = typedArray.getColor(
			R.styleable.Banner_cio_color,
			R.color.colorAccent.toInt()
		)
		
		this.updateColor()
		
		typedArray.recycle()
	}
	
	fun dismiss() = this.collapse()
	fun show() = this.expand()
	
	private fun updateColor()
	{
		if(this._backgroundColor != null)
		{
			this.setBackgroundColor(this._backgroundColor!!)
		}
		
		if(this._color != null)
		{
			this.lineView.setBackgroundColor(this._color!!)
		}
		
		if(this._icon != null && this._color != null)
		{
			this.iconView.icon!!.color(IconicsColor.colorInt(this._color!!))
		}
	}
	
	private fun View.expand()
	{
		this@expand.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
		val targetHeight = this@expand.measuredHeight
		
		this@expand.layoutParams.height = 0
		this@expand.visibility = View.VISIBLE
		val animation = object : Animation()
		{
			override fun applyTransformation(interpolatedTime: Float, t: Transformation)
			{
				this@expand.layoutParams.height = if (interpolatedTime == 1f)
					ViewGroup.LayoutParams.WRAP_CONTENT
				else
					(targetHeight * interpolatedTime).toInt()
				this@expand.requestLayout()
			}
			
			override fun willChangeBounds(): Boolean = true
		}
		
		animation.duration = (targetHeight / this@expand.context.resources.displayMetrics.density).toInt().toLong()
		this@expand.startAnimation(animation)
	}
	
	private fun View.collapse()
	{
		val initialHeight = this.measuredHeight
		
		val animation = object : Animation()
		{
			override fun applyTransformation(interpolatedTime: Float, t: Transformation)
			{
				if (interpolatedTime == 1f) {
					this@collapse.visibility = View.GONE
				} else {
					this@collapse.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
					this@collapse.requestLayout()
				}
			}
			
			override fun willChangeBounds(): Boolean = true
		}
		
		animation.duration = (initialHeight / this.context.resources.displayMetrics.density).toInt().toLong()
		this.startAnimation(animation)
	}
}