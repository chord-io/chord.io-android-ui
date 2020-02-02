package io.chord.ui.behaviors

import android.view.View
import io.chord.ui.components.ViewOrientation

open class OrientedBoundBehavior(
	private val view: View
)
{
	private lateinit var orientation: ViewOrientation
	
	fun getOrientation(): ViewOrientation
	{
		return this.orientation
	}
	
	fun setOrienation(orientation: ViewOrientation)
	{
		this.orientation = orientation
	}
	
	fun getSizeWithoutPadding(): Int
	{
		return if(this.orientation == ViewOrientation.Vertical)
		{
			this.view.height
		}
		else
		{
			this.view.width
		}
	}
	
	fun getSize(): Int
	{
		return this.getSizeWithoutPadding() - this.getPadding()
	}
	
	fun getPaddingStart(): Int
	{
		return if(this.orientation == ViewOrientation.Vertical)
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
		return if(this.orientation == ViewOrientation.Vertical)
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