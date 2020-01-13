package io.chord.ui.components

import android.view.View
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import io.chord.ui.utils.ViewUtils

class ScrollBarController(
	private val id: Int,
	private val scrollBar: ScrollBar
)
{
	private val scrollView: FrameLayout
	
	init
	{
		val rootView = ViewUtils.getParentRootView(scrollBar)
		this.scrollView = when(val scrollView = rootView.findViewById<View>(id))
		{
			is TwoDimensionalScrollView ->
			{
				if(scrollBar.orientation == ViewOrientation.Vertical)
				{
					scrollView.verticalScrollView
				} else
				{
					scrollView.horizontalScrollView
				}
			}
			is ScrollView -> scrollView
			is HorizontalScrollView -> scrollView
			else -> throw IllegalStateException("view is not a scrollview")
		}
	}
	
	override fun equals(other: Any?): Boolean
	{
		if(other is ScrollBarController)
		{
			when
			{
				this.getSizeWithoutPadding() != other.getSizeWithoutPadding() ->
				{
					return false
				}
				this.getContentSize() != other.getContentSize() ->
				{
					return false
				}
				this.getPaddingLeft() != other.getPaddingLeft() ->
				{
					return false
				}
				this.getPaddingRight() != other.getPaddingRight() ->
				{
					return false
				}
				else -> return true
			}
		}
		else
		{
			return false
		}
	}
	
	override fun hashCode(): Int
	{
		return (
			this.getSizeWithoutPadding() +
			this.getContentSize() +
			this.getPaddingLeft() +
			this.getPaddingRight()
		).hashCode()
	}
	
	fun getContentSize(): Int
	{
		if(this.scrollView.childCount > 0)
		{
			val child = scrollView.getChildAt(0)
			
			return if(this.scrollBar.orientation == ViewOrientation.Vertical)
			{
				child.height
			}
			else
			{
				child.width
			}
		}
		
		return this.getSizeWithoutPadding()
	}
	
	fun getSizeWithoutPadding(): Int
	{
		return if(this.scrollBar.orientation == ViewOrientation.Vertical)
		{
			this.scrollView.height
		}
		else
		{
			this.scrollView.width
		}
	}
	
	fun getSize(): Int
	{
		return this.getSizeWithoutPadding() - this.getPaddingLeft() - this.getPaddingRight()
	}
	
	fun getPaddingLeft(): Int
	{
		return if(this.scrollBar.orientation == ViewOrientation.Vertical)
		{
			this.scrollView.paddingTop
		}
		else
		{
			this.scrollView.paddingStart
		}
	}
	
	fun getPaddingRight(): Int
	{
		return if(this.scrollBar.orientation == ViewOrientation.Vertical)
		{
			this.scrollView.paddingBottom
		}
		else
		{
			this.scrollView.paddingEnd
		}
	}
	
	fun getPosition(): Int
	{
		return if(this.scrollBar.orientation == ViewOrientation.Vertical)
		{
			this.scrollView.scrollY
		}
		else
		{
			this.scrollView.scrollX
		}
	}
	
	fun setPosition(position: Int)
	{
		return if(this.scrollBar.orientation == ViewOrientation.Vertical)
		{
			this.scrollView.scrollY = position
		}
		else
		{
			this.scrollView.scrollX = position
		}
	}
}