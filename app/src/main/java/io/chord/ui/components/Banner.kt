package io.chord.ui.components

import android.annotation.SuppressLint
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
import io.chord.ui.utils.RippleDrawableUtils
import kotlinx.android.synthetic.main.component_banner.view.*

class Banner : LinearLayout
{
	private lateinit var _message: String
	private lateinit var _icon: String
	private var _color: Int = 0
	private var _leftButtonText: String? = null
	private var _rightButtonText: String? = null
	
	var message: String
		get() = this._message
		set(value) {
			this._message = value
			this.messageView.text = value
		}
	
	@Suppress("SetterBackingFieldAssignment")
	var messageId: Int? = null
		set(value) {
			if(value != null)
			{
				this.message = this.context.getString(value)
			}
		}
	
	var icon: String
		get() = this._icon
		set(value) {
			this._icon = value
			this.iconView.icon = IconicsDrawable(this.context).icon(value)
			this.updateColor()
		}
	
	var color: Int
		get() = this._color
		set(value) {
			this._color = value
			this.updateColor()
		}
	
	var leftButtonText: String?
		get() = this._leftButtonText
		set(value) {
			this._leftButtonText = value
			this.leftButton.text = value
			
			if(value == null)
			{
				this.leftButton.visibility = View.GONE
			}
			else
			{
				this.leftButton.visibility = View.VISIBLE
			}
			
			this.updateButtons()
		}
	
	var rightButtonText: String?
		get() = this._rightButtonText
		set(value) {
			this._rightButtonText = value
			this.rightButton.text = value
			
			if(value == null)
			{
				this.rightButton.visibility = View.GONE
			}
			else
			{
				this.rightButton.visibility = View.VISIBLE
			}
			
			this.updateButtons()
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
		View.inflate(this.context, R.layout.component_banner, this)
		
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.Banner, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this.message = typedArray.getString(
			R.styleable.Banner_cio_bn_message
		) ?: ""
		
		this.icon = typedArray.getString(
			R.styleable.Banner_cio_bn_icon
		) ?: ""
		
		this.color = typedArray.getColor(
			R.styleable.Banner_cio_bn_color,
			this.resources.getColor(R.color.colorAccent, theme)
		)
		
		this.leftButtonText = typedArray.getString(
			R.styleable.Banner_cio_bn_left_button_text
		)
		
		this.rightButtonText = typedArray.getString(
			R.styleable.Banner_cio_bn_right_button_text
		)
		
		this.updateColor()
		this.updateButtons()
		
		typedArray.recycle()
	}
	
	fun dismiss() = this.collapse()
	fun show() = this.expand()
	
	fun setLeftButtonOnClickListener(listener: ((view: View) -> Unit))
	{
		this.leftButton.setOnClickListener(listener)
	}
	
	fun setRightButtonOnClickListener(listener: ((view: View) -> Unit))
	{
		this.rightButton.setOnClickListener(listener)
	}
	
	@SuppressLint("ResourceAsColor")
	private fun updateColor()
	{
		val transparentColor = this.resources.getColor(android.R.color.transparent, this.context.theme)
		this.lineView.setBackgroundColor(this.color)
		
		this.leftButton.apply {
			this.setTextColor(color)
			this.rippleColor = RippleDrawableUtils.getColorStateList(
				transparentColor,
				color
			)
		}
		
		this.rightButton.apply {
			this.setTextColor(color)
			this.rippleColor = RippleDrawableUtils.getColorStateList(
				transparentColor,
				color
			)
		}
		
		this.iconView.icon!!.color(IconicsColor.colorInt(this._color))
	}
	
	private fun updateButtons()
	{
		if(this.leftButtonText == null && this.rightButtonText == null)
		{
			this.buttonLayout.visibility = View.GONE
		}
		else
		{
			this.buttonLayout.visibility = View.VISIBLE
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