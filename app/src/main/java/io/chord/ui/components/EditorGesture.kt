package io.chord.ui.components

import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import androidx.core.graphics.toRect
import io.chord.R
import io.chord.clients.models.Track
import io.chord.ui.extensions.clamp
import io.chord.ui.extensions.dpToPixel
import io.chord.ui.extensions.toTransparent
import io.chord.ui.gestures.GestureDetector
import io.chord.ui.gestures.SimpleOnGestureListener

class EditorGesture(
	private val context: Context
)
{
	class Rectangle(
		val a: PointF,
		val b: PointF
	)
	{
		fun toSurface(): TouchSurface
		{
			val rect = RectF()
			
			if(this.a.x.compareTo(this.b.x) > 0)
			{
				rect.left = this.b.x
				rect.right = this.a.x
			}
			else
			{
				rect.left = this.a.x
				rect.right = this.b.x
			}
			
			if(this.a.y.compareTo(this.b.y) > 0)
			{
				rect.top = this.b.y
				rect.bottom = this.a.y
			}
			else
			{
				rect.top = this.a.y
				rect.bottom = this.b.y
			}
			
			return TouchSurface(rect)
		}
	}
	
	open class TouchSurface(
		val surface: RectF,
		var isSelected: Boolean = false
	)
	
	class BarTouchSurface(
		surface: RectF,
		val index: Int
	) : TouchSurface(surface)
	
	class SequenceTouchSurface(
		surface: RectF,
		val track: Track,
		val index: Int
	) : TouchSurface(surface)
	
	class SurfaceDrawer(
		private val gesture: EditorGesture
	)
	{
		private var _frame: GradientDrawable? = null
		var onInvalidate: (() -> Unit)? = null
		
		val frame: Drawable?
			get() = this._frame
		
		fun draw(surface: TouchSurface)
		{
			if(this._frame == null)
			{
				// TODO : this as parameter
				val color = this.gesture.context.resources.getColor(
					R.color.colorAccent,
					this.gesture.context.theme
				)
				this._frame = GradientDrawable()
				// TODO : made this as parameter
				this._frame!!.setStroke(1f.dpToPixel().toInt(), color.toTransparent(0.5f))
				this._frame!!.setColor(color.toTransparent(0.25f))
			}
			
			this._frame!!.bounds = surface.surface.toRect().clamp(this.gesture.bounds)
			this.onInvalidate?.invoke()
		}
		
		fun clear()
		{
			this._frame = null
			this.onInvalidate?.invoke()
		}
	}
	
	val bounds: Rect = Rect()
	val surfaces: MutableList<TouchSurface> = mutableListOf()
	val drawer = SurfaceDrawer(this)
	private var detector: GestureDetector? = null
	
	fun onTouchEvent(event: MotionEvent): Boolean
	{
		if(this.detector != null)
		{
			return this.detector!!.onTouchEvent(event)
		}
		
		return false
	}
	
	fun intersect(surface: TouchSurface)
	{
		this.surfaces.forEach {
			it.isSelected = false
		}
		this.surfaces.filter {
			RectF.intersects(it.surface, surface.surface)
		}
		.forEach {
			it.isSelected = true
		}
	}
	
	fun setListener(listener: SimpleOnGestureListener)
	{
		this.detector = GestureDetector(
			this.context,
			listener
		)
	}
	
	fun clearListener()
	{
		this.detector = null
	}
}