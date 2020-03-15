package io.chord.ui.components

import android.content.Context
import android.util.AttributeSet
import io.chord.ui.extensions.toTransparent

class PianoRoll : KeyboardList
{
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
	
	override fun onFinishInflate()
	{
		super.onFinishInflate()
		this.whiteKeyColor = this.whiteKeyColor.toTransparent(0.1f)
		this.blackKeyColor = this.blackKeyColor.toTransparent(0.1f)
		this.strokeColor = this.strokeColor.toTransparent(0.1f)
	}
}