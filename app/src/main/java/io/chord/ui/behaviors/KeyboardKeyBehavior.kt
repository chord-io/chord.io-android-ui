package io.chord.ui.behaviors

import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import io.chord.ui.components.ViewOrientation

class KeyboardKeyBehavior
{
	abstract class Key
	{
		// h.r = w
		// w/r = h
		// w/h = r
		// wk.R = 0.28
		// bk.R = 0.22
		
		protected var _width: Float = 0f
		protected var _height: Float = 0f
		
		val width: Float
			get() = this._width
		
		val height: Float
			get() = this._height
		
		abstract fun setBounds(orientation: ViewOrientation, width: Float, height: Float, stroke: Float)
		abstract fun translate(orientation: ViewOrientation, index: Int, bounds: RectF, stroke: Float): RectF
	}
	
	open class WhiteKey : Key()
	{
		override fun setBounds(orientation: ViewOrientation, width: Float, height: Float, stroke: Float)
		{
			this._width = width - stroke
			this._height = height - stroke
		}
		
		override fun translate(orientation: ViewOrientation, index: Int, bounds: RectF, stroke: Float): RectF
		{
			// TODO: refactor components to use private class
			// TODO: refactor codes to look like this
			
			val halfStroke = stroke / 2f
			val rect = RectF()
			val left: Float
			val right: Float
			val top: Float
			val bottom: Float
			
			if(orientation == ViewOrientation.Horizontal)
			{
				left = this.width * index + bounds.left + halfStroke
				right = left + this.width
				top = bounds.top + halfStroke
				bottom = top + this.height - halfStroke
			}
			else
			{
				left = bounds.left + halfStroke
				right = left + this.width - halfStroke
				top = this.height * index + bounds.top + halfStroke
				bottom = top + this.height
			}
			
			rect.set(
				left,
				top,
				right,
				bottom
			)
			
			return rect
		}
	}
	
	class BlackKey : WhiteKey()
	{
		private var whiteKey = WhiteKey()
		
		override fun setBounds(orientation: ViewOrientation, width: Float, height: Float, stroke: Float)
		{
			this.whiteKey.setBounds(orientation, width, height, stroke)
			
			if(orientation == ViewOrientation.Horizontal)
			{
				this._width = this.whiteKey.width / 2f
				this._height = this.whiteKey.height
			}
			else
			{
				this._width = this.whiteKey.width
				this._height = this.whiteKey.height / 2f
			}
		}
		
		override fun translate(orientation: ViewOrientation, index: Int, bounds: RectF, stroke: Float): RectF
		{
			val parent =  this.whiteKey.translate(orientation, index, bounds, stroke)
			val rect = super.translate(orientation, index, bounds, stroke)
			
			if(orientation == ViewOrientation.Horizontal)
			{
				val left = parent.left + (this.width * 1.5f)
				val right = left + this.width
				
				rect.set(
					left,
					rect.top,
					right,
					rect.bottom
				)
			}
			else
			{
				val top = parent.top + (this.height * 1.5f)
				val bottom = top + this.height
				
				rect.set(
					rect.left,
					top,
					rect.right,
					bottom
				)
			}
			
			return rect
		}
	}
	
	val white = WhiteKey()
	val black = BlackKey()
	
	fun measure(
		orientation: ViewOrientation,
		layoutParameter: ViewGroup.LayoutParams,
		widthMeasureSpec: Float,
		heightMeasureSpec: Float,
		stroke: Float
	)
	{
		val width: Float
		val height: Float
		
		if(orientation == ViewOrientation.Horizontal)
		{
			width = widthMeasureSpec
			
			if(layoutParameter.height == ViewGroup.LayoutParams.MATCH_PARENT)
			{
				height = View.MeasureSpec.getSize(heightMeasureSpec.toInt()).toFloat()
			}
			else if(layoutParameter.height == ViewGroup.LayoutParams.WRAP_CONTENT)
			{
				height = widthMeasureSpec / 0.28f
			}
			else
			{
				height = heightMeasureSpec
			}
		}
		else
		{
			height = heightMeasureSpec
			
			if(layoutParameter.width == ViewGroup.LayoutParams.MATCH_PARENT)
			{
				width = View.MeasureSpec.getSize(widthMeasureSpec.toInt()).toFloat()
			}
			else if(layoutParameter.width == ViewGroup.LayoutParams.WRAP_CONTENT)
			{
				
				width = heightMeasureSpec / 0.28f
			}
			else
			{
				width = this.getKeyHeight(orientation, layoutParameter)
			}
		}
		
		this.white.setBounds(orientation, width, height, stroke)
		this.black.setBounds(orientation, width, height, stroke)
	}
	
	fun getKeyWidth(
		orientation: ViewOrientation,
		layoutParameter: ViewGroup.LayoutParams
	): Float
	{
		return if(orientation == ViewOrientation.Horizontal)
		{
			layoutParameter.width.toFloat()
		}
		else
		{
			layoutParameter.height.toFloat()
		}
	}
	
	fun getKeyHeight(
		orientation: ViewOrientation,
		layoutParameter: ViewGroup.LayoutParams
	): Float
	{
		return if(orientation == ViewOrientation.Horizontal)
		{
			layoutParameter.height.toFloat()
		}
		else
		{
			layoutParameter.width.toFloat()
		}
	}
}