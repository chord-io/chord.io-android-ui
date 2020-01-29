package io.chord.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.toRectF
import io.chord.R
import io.chord.ui.utils.ViewUtils

class TrackControl : View
{
	private val painter: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

	private var _state: TrackControlState = TrackControlState.None
	private var _backgroundNormalColor: Int = -1
	private var _backgroundMuteColor: Int = -1
	private var _backgroundSoloColor: Int = -1
	private var _unselectedTextColor: Int = -1
	private var _selectedTextColor: Int = -1
	private var _roundness: Float = -1f
	private var _textSize: Float = -1f
	private var _textMute: String = ""
	private var _textSolo: String = ""
	
	var state: TrackControlState
		get() = this._state
		set(value) {
			this._state = value
			this.invalidate()
		}
	
	var backgroundNormalColor: Int
		get() = this._backgroundNormalColor
		set(value) {
			this._backgroundNormalColor = value
			this.invalidate()
		}
	
	var backgroundMuteColor: Int
		get() = this._backgroundMuteColor
		set(value) {
			this._backgroundMuteColor = value
			this.invalidate()
		}
	
	var backgroundSoloColor: Int
		get() = this._backgroundSoloColor
		set(value) {
			this._backgroundSoloColor = value
			this.invalidate()
		}
	
	var unselectedTextColor: Int
		get() = this._unselectedTextColor
		set(value) {
			this._unselectedTextColor = value
			this.invalidate()
		}
	
	var selectedTextColor: Int
		get() = this._selectedTextColor
		set(value) {
			this._selectedTextColor = value
			this.invalidate()
		}
	
	var roundness: Float
		get() = this._roundness
		set(value) {
			this._roundness = value
			this.invalidate()
		}
	
	var textSize: Float
		get() = this._textSize
		set(value) {
			this._textSize = value
			this.invalidate()
		}
	
	var textMute: String
		get() = this._textMute
		set(value) {
			this._textMute = value
			this.invalidate()
		}
	
	var textSolo: String
		get() = this._textSolo
		set(value) {
			this._textSolo = value
			this.invalidate()
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
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.TrackControl, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._state = typedArray.getInteger(
			R.styleable.TrackControl_cio_tc_state,
			TrackControlState.None.state
		).let {
			TrackControlState.values()[it]
		}
		
		this._backgroundNormalColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_backgroundNormalColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._backgroundMuteColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_backgroundMuteColor,
			this.resources.getColor(R.color.warningColor, theme)
		)
		
		this._backgroundSoloColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_backgroundSoloColor,
			this.resources.getColor(R.color.errorColor, theme)
		)
		
		this._unselectedTextColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_unselectedTextColor,
			this.resources.getColor(R.color.textColorSecondary, theme)
		)
		
		this._selectedTextColor = typedArray.getColor(
			R.styleable.TrackControl_cio_tc_selectedTextColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._roundness = typedArray.getDimension(
			R.styleable.TrackControl_cio_tc_roundness,
			this.resources.getDimension(R.dimen.track_control_roundness)
		)
		
		this._textSize = typedArray.getDimension(
			R.styleable.TrackControl_cio_tc_textSize,
			this.resources.getDimension(R.dimen.track_control_text_size)
		)
		
		this._textMute = typedArray.getString(
			R.styleable.TrackControl_cio_tc_textMute
		)!!
		
		this._textSolo = typedArray.getString(
			R.styleable.TrackControl_cio_tc_textSolo
		)!!

		typedArray.recycle()
	}
	
	override fun onTouchEvent(event: MotionEvent): Boolean
	{
		if(event.action == MotionEvent.ACTION_DOWN)
		{
			val position = event.x
			val halfWidth = this.width / 2f
			
			this.state = when
			{
				position <= halfWidth && this.state != TrackControlState.Mute -> TrackControlState.Mute
				position > halfWidth && this.state != TrackControlState.Solo -> TrackControlState.Solo
				else -> TrackControlState.None
			}
			
			return true
		}
		
		return super.onTouchEvent(event)
	}
	
	override fun onDraw(canvas: Canvas?)
	{
		this.drawMuteButton(canvas!!)
		this.drawSoloButton(canvas)
	}
	
	private fun drawMuteButton(canvas: Canvas)
	{
		val bounds = canvas.clipBounds.toRectF()
		
		bounds.set(
			bounds.left,
			bounds.top,
			bounds.right - bounds.width() / 2f,
			bounds.bottom
		)
		
		if(this.state == TrackControlState.Mute)
		{
			this.painter.color = this.backgroundMuteColor
		}
		else
		{
			this.painter.color = this.backgroundNormalColor
		}
		
		val corners = floatArrayOf(
			this.roundness, this.roundness,
			0f, 0f,
			0f, 0f,
			this.roundness, this.roundness
		)
		
		val path = Path()
		path.addRoundRect(bounds, corners, Path.Direction.CW)
		
		canvas.drawPath(
			path,
			this.painter
		)
		
		if(this.state == TrackControlState.Mute)
		{
			this.painter.color = this.selectedTextColor
		}
		else
		{
			this.painter.color = this.unselectedTextColor
		}
		
		this.painter.textSize = this.textSize
		
		val textPosition = ViewUtils.getTextCentered(
			this.textMute,
			bounds.centerX().toInt(),
			bounds.centerY().toInt(),
			this.painter
		)
		
		canvas.drawText(
			this.textMute,
			textPosition.x,
			textPosition.y,
			this.painter
		)
	}
	
	private fun drawSoloButton(canvas: Canvas)
	{
		val bounds = canvas.clipBounds.toRectF()
		
		bounds.set(
			bounds.left + bounds.width() / 2f,
			bounds.top,
			bounds.right,
			bounds.bottom
		)
		
		if(this.state == TrackControlState.Solo)
		{
			this.painter.color = this.backgroundSoloColor
		}
		else
		{
			this.painter.color = this.backgroundNormalColor
		}
		
		val corners = floatArrayOf(
			0f, 0f,
			this.roundness, this.roundness,
			this.roundness, this.roundness,
			0f, 0f
		)
		
		val path = Path()
		path.addRoundRect(bounds, corners, Path.Direction.CW)
		
		canvas.drawPath(
			path,
			this.painter
		)
		
		if(this.state == TrackControlState.Solo)
		{
			this.painter.color = this.selectedTextColor
		}
		else
		{
			this.painter.color = this.unselectedTextColor
		}
		
		this.painter.textSize = this.textSize
		
		val textPosition = ViewUtils.getTextCentered(
			this.textSolo,
			bounds.centerX().toInt(),
			bounds.centerY().toInt(),
			this.painter
		)
		
		canvas.drawText(
			this.textSolo,
			textPosition.x,
			textPosition.y,
			this.painter
		)
	}
}