package io.chord.ui.utils

import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.util.Size
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.core.view.forEach
import androidx.fragment.app.FragmentActivity
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
		
		fun invalidateParent(parent: ViewParent?)
		{
			when(parent)
			{
				null -> return
				is View -> parent.invalidate()
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
		
		fun dpToPixel(view: View, value: Float): Float
		{
			val metrics = view.context.resources.displayMetrics
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics)
		}
		
		fun spToPixel(view: View, value: Float): Float
		{
			val metrics = view.context.resources.displayMetrics
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics)
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
	}
}