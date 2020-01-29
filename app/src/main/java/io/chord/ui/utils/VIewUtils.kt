package io.chord.ui.utils

import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.FragmentActivity
import io.chord.ui.ChordIOApplication
import io.chord.ui.maths.LocalMaximumResolver
import io.chord.ui.maths.LocalOptimumResolver
import kotlin.collections.set

class ViewUtils
{
	companion object
	{
		fun setViewState(view: View, state: Boolean)
		{
			var depth = 0
			val views: MutableMap<Int, MutableList<View>> = mutableMapOf()
			views[depth] = mutableListOf(view)
			
			while(true)
			{
				val currentViews = views[depth]
				val nextViews = mutableListOf<View>()
				
				currentViews!!.forEach { view ->
					if(view is ViewGroup)
					{
						view.forEach { children ->
							nextViews.add(children)
						}
					}
				}
				
				if(nextViews.size == 0)
				{
					break
				}
				
				depth++
				
				views[depth] = nextViews
			}
			
			views.flatMap {
				it.value
			}.forEach {
				it.isEnabled = state
			}
		}
		
		fun getDirectChildrens(view: View): List<View>?
		{
			if(view is ViewGroup)
			{
				val list = mutableListOf<View>()
				view.forEach { children ->
					list.add(children)
				}
				return list
			}
			
			return null
		}
		
		fun getDirectChildren(view: View): View?
		{
			if(view is ViewGroup)
			{
				return view.getChildAt(0)
			}

			return null
		}
		
		fun getRootView(activity: FragmentActivity): View
		{
			val view = activity.findViewById<ViewGroup>(android.R.id.content)
			return view.getChildAt(0)
		}
		
		fun getParentRootView(view: View): View
		{
			var lastParent = view.parent
			
			while(true)
			{
				val parent = lastParent.parent
				
				if(parent != null && parent is View && parent is ViewGroup)
				{
					lastParent = parent
				}
				else
				{
					break
				}
			}
			
			return lastParent as View
		}
		
		fun dpToPixel(value: Float): Float
		{
			val metrics = ChordIOApplication.instance.resources.displayMetrics
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics)
		}
		
		fun pixelToDp(value: Float): Float
		{
			val metrics = ChordIOApplication.instance.resources.displayMetrics
			return value / metrics.density;
		}
		
		fun spToPixel(value: Float): Float
		{
			val metrics = ChordIOApplication.instance.resources.displayMetrics
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics)
		}
		
		fun pixelToSp(value: Float): Float
		{
			val metrics = ChordIOApplication.instance.resources.displayMetrics
			return value / metrics.scaledDensity;
		}
		
		fun getTextWidth(text: String, painter: Paint): Int
		{
			val bounds = Rect()
			painter.getTextBounds(text, 0, text.count(), bounds)
			return bounds.width()
		}
		
		fun getTextHeight(text: String, painter: Paint): Int
		{
			val bounds = Rect()
			painter.getTextBounds(text, 0, text.count(), bounds)
			return bounds.height()
		}
		
		fun getTextCentered(
			text: String,
			x: Int,
			y: Int,
			painter: Paint
		): PointF
		{
			val newX = x - painter.measureText(text) / 2f
			val newY = y - (painter.descent() + painter.ascent()) / 2f
			return PointF(newX, newY)
		}
		
		fun getOptimalTextSize(
			text: String,
			textSize: Float,
			height: Float,
			painter: Paint
		): Float
		{
			val resolver = LocalOptimumResolver(textSize, 0.001f)
			val bounds = Rect()
			val clonedPainter = Paint(painter)
			
			return resolver.resolve { value ->
				clonedPainter.textSize = value
				clonedPainter.getTextBounds(text, 0, text.length, bounds)
				
				var spSize = pixelToSp(value)
				
				if(bounds.height() > height)
				{
					spSize -= 1
				}
				else if(bounds.height() <= height)
				{
					spSize += 1
				}
				
				spToPixel(spSize)
			}
		}
		
		fun getMaximumTextSize(
			text: String,
			textSize: Float,
			height: Float,
			painter: Paint
		): Float
		{
			val resolver = LocalMaximumResolver(textSize, 0.001f)
			val bounds = Rect()
			val clonedPainter = Paint(painter)
			
			return resolver.resolve { value ->
				clonedPainter.textSize = value
				clonedPainter.getTextBounds(text, 0, text.length, bounds)
				
				var spSize = pixelToSp(value)
				
				if(bounds.height() > height)
				{
					spSize -= 1
				}
				
				spToPixel(spSize)
			}
		}
	}
}