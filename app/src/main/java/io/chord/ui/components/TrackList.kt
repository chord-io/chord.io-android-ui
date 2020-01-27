package io.chord.ui.components

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.clients.models.Track
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.models.TrackListItemViewModel
import io.chord.ui.utils.ViewUtils

class TrackList : LinearLayout
{
	private class TrackListDataSetObserver(
		private val trackList: TrackList
	) : DataSetObserver()
	{
		private fun createView()
		{
			this.trackList.removeAllViews()
			
			val count = this.trackList.adapter.count
			
			for(index in 0 until count)
			{
				val view = this.trackList.adapter.getView(
					index,
					null,
					this.trackList
				)
				
				this.trackList.addView(view)
			}
		}
		
		override fun onChanged()
		{
			this.createView()
		}
		
		override fun onInvalidated()
		{
			this.createView()
		}
	}
	
	private val adapter: TrackListAdapter = TrackListAdapter(this.context)
	
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
			attrs, R.styleable.TrackList, defStyle, 0
		)
		
		typedArray.recycle()
		
		this.orientation = LinearLayout.VERTICAL
		
		this.adapter.registerDataSetObserver(TrackListDataSetObserver(this))
	}
	
	fun add(track: Track)
	{
		this.adapter.add(track)
	}
	
	fun remove(track: Track)
	{
		this.adapter.remove(track)
	}
}