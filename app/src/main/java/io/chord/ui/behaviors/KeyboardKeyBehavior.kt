package io.chord.ui.behaviors

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toRectF
import io.chord.ui.components.ViewOrientation
import io.chord.ui.extensions.addIfNotPresent

class KeyboardKeyBehavior
{
	class WhiteKeyTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	{
		override fun equals(other: Any?): Boolean
		{
			if(other is WhiteKeyTouchSurface)
			{
				val isRectangleEqual = this.rectangle == other.rectangle
				val isIndexEqual = this.index == other.index
				return isRectangleEqual && isIndexEqual
			}
			
			return false
		}
		
		override fun hashCode(): Int
		{
			return super.hashCode()
		}
		
		fun toIntegral(): Int
		{
			var note = this.index * 2
			if(note >= 6)
			{
				note -= 1
			}
			return note
		}
	}
	
	class BlackKeyTouchSurface(
		rectangle: RectF,
		val index: Int
	) : SurfaceGestureBehavior.TouchSurface(rectangle)
	{
		override fun equals(other: Any?): Boolean
		{
			if(other is BlackKeyTouchSurface)
			{
				val isRectangleEqual = this.rectangle == other.rectangle
				val isIndexEqual = this.index == other.index
				return isRectangleEqual && isIndexEqual
			}
			
			return false
		}
		
		override fun hashCode(): Int
		{
			return super.hashCode()
		}
		
		fun toIntegral(): Int
		{
			var note = this.index * 2
			if(this.index < 3)
			{
				note += 1
			}
			return note
		}
	}
	
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
		
		abstract fun getProgression(orientation: ViewOrientation): IntProgression
		abstract fun convertIndex(orientation: ViewOrientation, index: Int): Int
		abstract fun measure(orientation: ViewOrientation, width: Float, height: Float, stroke: Float, clamp: Boolean)
		abstract fun translate(orientation: ViewOrientation, index: Int, bounds: RectF, stroke: Float, clamp: Boolean): RectF
		abstract fun draw(
			canvas: Canvas,
			painter: Paint,
			surfaces: MutableList<SurfaceGestureBehavior.TouchSurface>,
			orientation: ViewOrientation,
			stroke: Float,
			weight: Float = 1f,
			clamp: Boolean,
			maskColor: Int,
			fillColor: Int,
			strokeColor: Int,
			touchColor: Int,
			onDraw: ((RectF, Int) -> Unit)? = null
		)
	}
	
	open class WhiteKey : Key()
	{
		override fun getProgression(orientation: ViewOrientation): IntProgression
		{
			return if(orientation == ViewOrientation.Horizontal)
			{
				0 until 7
			}
			else
			{
				6 downTo 0
			}
		}
		
		override fun convertIndex(orientation: ViewOrientation, index: Int): Int
		{
			return if(orientation == ViewOrientation.Horizontal)
			{
				return index
			}
			else
			{
				6 - index
			}
		}
		
		override fun measure(orientation: ViewOrientation, width: Float, height: Float, stroke: Float, clamp: Boolean)
		{
			this._width = width
			this._height = height
		}
		
		override fun translate(orientation: ViewOrientation, index: Int, bounds: RectF, stroke: Float, clamp: Boolean): RectF
		{
			// TODO: refactor components to use private class
			// TODO: refactor codes to look like this
			val rect = RectF()
			val left: Float
			val right: Float
			val top: Float
			val bottom: Float
			val margin = if(clamp)
			{
				0f
			}
			else
			{
				stroke
			}
			
			if(orientation == ViewOrientation.Horizontal)
			{
				left = ((this.width + stroke) * index) + bounds.left + margin
				right = left + this.width
				top = bounds.top + margin
				bottom = bounds.bottom - margin
			}
			else
			{
				left = bounds.left + margin
				right = bounds.right - margin
				top = ((this.height + stroke) * index) + bounds.top + margin
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
		
		override fun draw(
			canvas: Canvas,
			painter: Paint,
			surfaces: MutableList<SurfaceGestureBehavior.TouchSurface>,
			orientation: ViewOrientation,
			stroke: Float,
			weight: Float,
			clamp: Boolean,
			maskColor: Int,
			fillColor: Int,
			strokeColor: Int,
			touchColor: Int,
			onDraw: ((RectF, Int) -> Unit)?
		)
		{
			val keys = surfaces.filterIsInstance(
				WhiteKeyTouchSurface::class.java
			)
			val bounds = canvas.clipBounds.toRectF()
			
			for(index in this.getProgression(orientation))
			{
				painter.strokeWidth = stroke
				
				val surface = keys.firstOrNull {
					it.index == index
				}
				val rectangle = this.translate(
					orientation,
					this.convertIndex(orientation, index),
					bounds,
					stroke,
					clamp
				)
				
				painter.color = maskColor
				painter.style = Paint.Style.FILL
				canvas.drawRect(rectangle, painter)
				
				if(surface != null && surface.isSelected)
				{
					painter.color = touchColor
					painter.style = Paint.Style.FILL
				}
				else
				{
					surfaces.addIfNotPresent(WhiteKeyTouchSurface(
						rectangle,
						index
					))
					
					painter.color = fillColor
					painter.style = Paint.Style.FILL
				}
				
				canvas.drawRect(rectangle, painter)
				
				onDraw?.invoke(rectangle, index)
			}
		}
	}
	
	class BlackKey : WhiteKey()
	{
		private var whiteKey = WhiteKey()
		
		override fun getProgression(orientation: ViewOrientation): IntProgression
		{
			return if(orientation == ViewOrientation.Horizontal)
			{
				0 until 6
			}
			else
			{
				5 downTo 0
			}
		}
		
		override fun convertIndex(orientation: ViewOrientation, index: Int): Int
		{
			return if(orientation == ViewOrientation.Horizontal)
			{
				return index
			}
			else
			{
				5 - index
			}
		}
		
		override fun measure(orientation: ViewOrientation, width: Float, height: Float, stroke: Float, clamp: Boolean)
		{
			this.whiteKey.measure(orientation, width, height, stroke, clamp)
			
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
		
		override fun translate(orientation: ViewOrientation, index: Int, bounds: RectF, stroke: Float, clamp: Boolean): RectF
		{
			val halfStroke = stroke / 2f
			val parent =  this.whiteKey.translate(orientation, index, bounds, stroke, clamp)
			val rect = super.translate(orientation, index, bounds, stroke, clamp)
			
			if(orientation == ViewOrientation.Horizontal)
			{
				val left = if(index == 0 || index == 4)
				{
					(parent.right + halfStroke) - rect.width() * 0.75f
				}
				else if(index == 2)
				{
					(parent.right + halfStroke) - rect.width() * 0.25f
				}
				else
				{
					(parent.right + halfStroke) - rect.width() / 2f
				}
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
				val top = if(index == 0 || index == 4)
				{
					(parent.bottom + halfStroke) - rect.height() * 0.75f
				}
				else if(index == 2)
				{
					(parent.bottom + halfStroke) - rect.height() * 0.25f
				}
				else
				{
					(parent.bottom + halfStroke) - rect.height() / 2f
				}
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
		
		override fun draw(
			canvas: Canvas,
			painter: Paint,
			surfaces: MutableList<SurfaceGestureBehavior.TouchSurface>,
			orientation: ViewOrientation,
			stroke: Float,
			weight: Float,
			clamp: Boolean,
			maskColor: Int,
			fillColor: Int,
			strokeColor: Int,
			touchColor: Int,
			onDraw: ((RectF, Int) -> Unit)?
		)
		{
			val keys = surfaces.filterIsInstance(
				BlackKeyTouchSurface::class.java
			)
			val bounds = canvas.clipBounds.toRectF()
			
			painter.strokeWidth = stroke
			
			for(index in this.getProgression(orientation))
			{
				if(index == 2)
				{
					continue
				}
				
				val rectangle = this.translate(
					orientation,
					this.convertIndex(orientation, index),
					bounds,
					stroke,
					clamp
				)
				
				if(orientation == ViewOrientation.Horizontal)
				{
					rectangle.set(
						rectangle.left,
						rectangle.top,
						rectangle.right,
						rectangle.bottom * weight
					)
				}
				else
				{
					rectangle.set(
						rectangle.left,
						rectangle.top,
						rectangle.right * weight,
						rectangle.bottom
					)
				}
				
				val surface = keys.firstOrNull {
					it.index == index
				}
				
				painter.color = maskColor
				painter.style = Paint.Style.FILL
				canvas.drawRect(rectangle, painter)
				
				if(surface != null && surface.isSelected)
				{
					painter.color = touchColor
					painter.style = Paint.Style.FILL
					canvas.drawRect(rectangle, painter)
					
					painter.color = strokeColor
					painter.style = Paint.Style.STROKE
					canvas.drawRect(rectangle, painter)
				}
				else
				{
					surfaces.addIfNotPresent(
						BlackKeyTouchSurface(
							rectangle,
							index
						)
					)
					
					painter.color = fillColor
					painter.style = Paint.Style.FILL
					canvas.drawRect(rectangle, painter)
				}
				
				onDraw?.invoke(rectangle, index)
			}
		}
	}
	
	val white = WhiteKey()
	val black = BlackKey()
	
	fun measure(
		orientation: ViewOrientation,
		layoutParameter: ViewGroup.LayoutParams,
		widthMeasureSpec: Number,
		heightMeasureSpec: Number,
		stroke: Float,
		clamp: Boolean
	)
	{
		val width: Float
		val height: Float
		
		if(orientation == ViewOrientation.Horizontal)
		{
			width = widthMeasureSpec.toFloat()
			
			if(layoutParameter.height == ViewGroup.LayoutParams.MATCH_PARENT)
			{
				height = View.MeasureSpec.getSize(heightMeasureSpec.toInt()).toFloat()
			}
			else if(layoutParameter.height == ViewGroup.LayoutParams.WRAP_CONTENT)
			{
				height = widthMeasureSpec.toFloat() / 0.28f
			}
			else
			{
				height = this.getKeyHeight(orientation, layoutParameter)
			}
		}
		else
		{
			height = heightMeasureSpec.toFloat()
			
			if(layoutParameter.width == ViewGroup.LayoutParams.MATCH_PARENT)
			{
				width = View.MeasureSpec.getSize(widthMeasureSpec.toInt()).toFloat()
			}
			else if(layoutParameter.width == ViewGroup.LayoutParams.WRAP_CONTENT)
			{
				
				width = heightMeasureSpec.toFloat() / 0.28f
			}
			else
			{
				width = this.getKeyHeight(orientation, layoutParameter)
			}
		}
		
		this.white.measure(orientation, width, height, stroke, clamp)
		this.black.measure(orientation, width, height, stroke, clamp)
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