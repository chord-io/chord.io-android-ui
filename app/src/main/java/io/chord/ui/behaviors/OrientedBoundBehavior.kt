package io.chord.ui.behaviors

import android.view.View
import io.chord.ui.components.ViewOrientation

open class OrientedBoundBehavior(
	private val view: View
)
{
	private lateinit var _orientation: ViewOrientation
	
	var orientation: ViewOrientation
		get() = this._orientation
		set(value) {
			this._orientation = value
		}
	
	open fun getSizeWithoutPadding(): Int
	{
		return if(this._orientation == ViewOrientation.Vertical)
		{
			this.view.height
		}
		else
		{
			this.view.width
		}
	}
	
	open fun getSize(): Int
	{
		return this.getSizeWithoutPadding() - this.getPadding()
	}
	
	fun getPaddingStart(): Int
	{
		return if(this._orientation == ViewOrientation.Vertical)
		{
			this.view.paddingTop
		}
		else
		{
			this.view.paddingStart
		}
	}
	
	fun getPaddingEnd(): Int
	{
		return if(this._orientation == ViewOrientation.Vertical)
		{
			this.view.paddingBottom
		}
		else
		{
			this.view.paddingEnd
		}
	}
	
	fun getPadding(): Int
	{
		return this.getPaddingStart() + this.getPaddingEnd()
	}
}