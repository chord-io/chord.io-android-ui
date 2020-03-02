package io.chord.ui.components

import android.animation.LayoutTransition
import android.content.ClipData
import android.content.Context
import android.database.DataSetObserver
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import io.chord.R
import io.chord.ui.behaviors.BindBehavior
import io.chord.ui.behaviors.Bindable
import io.chord.ui.behaviors.BindableBehavior
import io.chord.ui.behaviors.Binder
import io.chord.ui.behaviors.ZoomBehavior
import io.chord.ui.extensions.getDirectChildrens

abstract class ListView<TModel, TViewModel: ListViewModel, TViewHolder: ListViewHolder<TModel, TViewModel>> : LinearLayout, Zoomable, Binder
{
	private val onDragListener = View.OnDragListener { view, event ->
		val index = this.indexOfChild(view)
		val eventIndex = if(this.draggedItem != null)
		{
			this.indexOfChild(this.draggedItem)
		}
		else
		{
			event.localState as Int
		}
		
		if(index == -1 || eventIndex == index)
		{
			return@OnDragListener false
		}
		
		when (event.action) {
			DragEvent.ACTION_DRAG_STARTED -> {
				this.draggedItem = this.getDirectChildrens()!![eventIndex]
				this.draggedItem!!.visibility = View.INVISIBLE
				return@OnDragListener true
			}
			DragEvent.ACTION_DRAG_ENTERED -> {
				if(this.draggedItem == null)
				{
					return@OnDragListener false
				}
				this.animateOnChangeLayout(true)
				this.removeView(this.draggedItem)
				this.addView(this.draggedItem, index)
				this.animateOnChangeLayout(false)
				return@OnDragListener true
			}
			DragEvent.ACTION_DRAG_ENDED -> {
				if(this.draggedItem == null)
				{
					return@OnDragListener false
				}
				this.draggedItem!!.visibility = View.VISIBLE
				this.draggedItem = null
				val from = event.localState as Int
				val to = eventIndex
				val notifyOnChange = this.adapter.getNotifyOnChange()
				this.adapter.setNotifyOnChange(false)
				this.adapter.move(from, to)
				this.adapter.setNotifyOnChange(notifyOnChange)
				this.bindBehavior.requestDispatchEvent()
				this.listener.onDragEnded(from, to)
				return@OnDragListener true
			}
			else -> return@OnDragListener false
		}
	}
	
	private val onLongClickListener = View.OnLongClickListener { view: View ->
		val index = this.indexOfChild(view)
		val data = ClipData.newPlainText(null, index.toString())
		val shadow = View.DragShadowBuilder(view)
		view.startDragAndDrop(data, shadow, index, 0)
		true
	}
	
	private class ListViewDataSetObserver(
		private val list: ListView<*, *, *>
	) : DataSetObserver()
	{
		override fun onChanged()
		{
			this.list.populateLayout()
			this.list.bindBehavior.requestDispatchEvent()
		}
		
		override fun onInvalidated()
		{
			this.list.populateLayout()
			this.list.bindBehavior.requestDispatchEvent()
		}
	}
	
	private var draggedItem: View? = null
	private val zoomBehavior: ZoomBehavior = ZoomBehavior()
	private lateinit var bindableBehavior: BindableBehavior
	private val divider: ShapeDrawable = ShapeDrawable()
	private lateinit var bindBehavior: BindBehavior<Listable<TModel>>
	protected val adapter: ListAdapter<TModel, TViewModel> = ListAdapter(this.context)
	
	var listener: ListClickListener<TModel>
		get() = this.adapter.listener
		set(value) {
			this.adapter.listener = value
		}
	
	private var _zoomDuration: Long = -1
	private var _dividerColor: Int = -1
	private var _dividerThickness: Float = -1f
	private var _rowHeight: Float = -1f
	private var _rowPadding: Float = -1f
	
	var zoomDuration: Long
		get() = this._zoomDuration
		set(value) {
			this._zoomDuration = value
			this.zoomBehavior.heightAnimator.duration = value
		}
	
	var dividerColor: Int
		get() = this._dividerColor
		set(value) {
			this._dividerColor = value
			this.invalidateDividerDrawable()
		}
	
	var dividerThickness: Float
		get() = this._dividerThickness
		set(value) {
			this._dividerThickness = value
			this.invalidateDividerDrawable()
		}
	
	var rowHeight: Float
		get() = this._rowHeight
		set(value) {
			this._rowHeight = value
			this.setZoomFactor(
				ViewOrientation.Vertical,
				this.zoomBehavior.getFactorHeight(),
				true
			)
		}
	
	var rowPadding: Float
		get() = this._rowPadding
		set(value) {
			this._rowPadding = value
			this.requestLayout()
		}
	
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
	
	protected open fun init(attrs: AttributeSet?, defStyle: Int)
	{
		val typedArray = context.obtainStyledAttributes(
			attrs, R.styleable.ListView, defStyle, 0
		)
		
		val theme = this.context.theme
		
		this._zoomDuration = typedArray.getInteger(
			R.styleable.ListView_cio_lv_zoomDuration,
			this.resources.getInteger(R.integer.list_view_zoom_duration)
		).toLong()
		
		this._dividerColor = typedArray.getColor(
			R.styleable.ListView_cio_lv_dividerColor,
			this.resources.getColor(R.color.backgroundPrimary, theme)
		)
		
		this._dividerThickness = typedArray.getDimension(
			R.styleable.ListView_cio_lv_dividerThickness,
			this.resources.getDimension(R.dimen.list_view_divider_thickness)
		)
		
		this._rowHeight = typedArray.getDimension(
			R.styleable.ListView_cio_lv_rowHeight,
			this.resources.getDimension(R.dimen.list_view_row_height)
		)
		
		this._rowPadding = typedArray.getDimension(
			R.styleable.ListView_cio_lv_rowPadding,
			this.resources.getDimension(R.dimen.list_view_row_padding)
		)
		
		typedArray.recycle()
		
		// TODO: handle horizontal orientation
		this.orientation = VERTICAL
		
		this.adapter.registerDataSetObserver(ListViewDataSetObserver(this))
		
		this.bindableBehavior = BindableBehavior(this)
		
		this.bindBehavior = BindBehavior(this)
		this.bindBehavior.onAttach = {}
		this.bindBehavior.onDispatchEvent = {
			it.setDataSet(this.adapter.items)
		}
		
		this.zoomBehavior.heightAnimator.duration = this.zoomDuration
		
		this.zoomBehavior.onEvaluateHeight = this::rowHeight
		this.zoomBehavior.onMeasureHeight = this::onMeasureChange
		
		this.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
			this.invalidateDividerDrawable()
		}
	}
	
	override fun attach(id: Int)
	{
		this.bindBehavior.attach(id)
	}
	
	override fun attach(view: View)
	{
		this.bindBehavior.attach(view)
	}
	
	override fun attach(fragment: Fragment)
	{
		this.bindBehavior.attach(fragment)
	}
	
	override fun attachAll(views: List<View>)
	{
		this.bindBehavior.attachAll(views)
	}
	
	override fun detach(id: Int)
	{
		this.bindBehavior.detach(id)
	}
	
	override fun detachAll()
	{
		this.bindBehavior.detachAll()
	}
	
	fun requestDispatchEvent()
	{
		this.bindBehavior.requestDispatchEvent()
	}
	
	override fun attach(controller: BindBehavior<Bindable>)
	{
		this.bindableBehavior.attach(controller)
	}
	
	override fun selfAttach()
	{
		this.bindableBehavior.selfAttach()
	}
	
	override fun selfDetach()
	{
		this.bindableBehavior.selfDetach()
	}
	
	fun add(track: TModel)
	{
		this.adapter.add(track)
	}
	
	fun addAll(tracks: List<TModel>)
	{
		this.adapter.addAll(tracks.toMutableList())
	}
	
	fun update(track: TModel)
	{
		this.adapter.update(track)
	}
	
	fun remove(track: TModel)
	{
		this.adapter.remove(track)
	}
	
	private fun invalidateDividerDrawable()
	{
		this.divider.intrinsicHeight = this.dividerThickness.toInt()
		this.divider.setColorFilter(this.dividerColor, PorterDuff.Mode.SRC)
		this.dividerDrawable = this.divider
	}
	
	@Suppress("UNCHECKED_CAST")
	protected fun populateLayout()
	{
		this.adapter.recycleViews()
		this.removeAllViews()
		
		val height = this.zoomBehavior.factorizedHeight.toInt()
		val padding = this.rowPadding.toInt()
		val count = this.adapter.count
		
		for(index in 0 until count)
		{
			val view = this.adapter.getView(
				index,
				null,
				this
			)
			
			view.setOnLongClickListener(this.onLongClickListener)
			view.setOnDragListener(this.onDragListener)
			
			val holder = this.adapter.getViewHolder(view)
			this.onViewHolder(holder as TViewHolder)
			this.adjustItemViewDimension(holder, height, padding)
			this.addView(view)
		}
	}
	
	protected abstract fun onViewHolder(holder: TViewHolder)
	
	private fun onMeasureChange()
	{
		val count = this.adapter.count
		
		if(count == 0)
		{
			return
		}
		
		val height = this.zoomBehavior.factorizedHeight.toInt()
		val padding = this.rowPadding.toInt()
		
		for(index in 0 until count)
		{
			val view = this.getChildAt(index)
			val holder = this.adapter.getViewHolder(view)
			this.adjustItemViewDimension(holder, height, padding)
		}
		
		this.requestLayout()
	}
	
	private fun adjustItemViewDimension(holder: ListViewHolder<TModel, TViewModel>, height: Int, padding: Int)
	{
		holder.view.setPadding(padding, padding, padding, padding)
		holder.view.layoutParams.height = height
	}
	
	override fun setZoomFactor(
		orientation: ViewOrientation,
		factor: Float,
		animate: Boolean
	)
	{
		if(orientation == ViewOrientation.Vertical)
		{
			this.zoomBehavior.setFactorHeight(factor, animate)
		}
	}
	
	private fun animateOnChangeLayout(state: Boolean)
	{
		if(state)
		{
			this.layoutTransition = LayoutTransition()
		}
		else
		{
			this.layoutTransition = null
		}
	}
}