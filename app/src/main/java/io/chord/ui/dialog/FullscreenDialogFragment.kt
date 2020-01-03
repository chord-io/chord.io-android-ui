package io.chord.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeRes
import io.chord.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.xml.bind.JAXBElement


open class FullscreenDialogFragment(
	private val layoutId: Int,
	protected var title: String? = null,
	private val loaderOnAccept: Boolean = false
) : DialogFragment()
{
	private lateinit var toolbar: Toolbar
	private lateinit var rootView: View
	private lateinit var action: MenuItem
	
	var onLayoutCreatedListener: ((view: View?) -> Unit)? = null
	var onLayoutUpdatedListener: ((view: View?) -> Unit)? = null
	
	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		this.setStyle(
			STYLE_NORMAL,
			R.style.FullScreenDialog
		)
	}
	
	override fun onStart()
	{
		super.onStart()
		val dialog = this.dialog
		if(dialog != null)
		{
			val width = ViewGroup.LayoutParams.MATCH_PARENT
			val height = ViewGroup.LayoutParams.MATCH_PARENT
			dialog.window?.setLayout(width, height)
			dialog.window?.setWindowAnimations(R.style.AppTheme_Slide)
		}
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		super.onCreateView(inflater, container, savedInstanceState)
		
		val view = inflater.inflate(R.layout.dialog_fullscreen, null)
		this.toolbar = view.findViewById(R.id.toolbar)
		
		val layout = view.findViewById<ViewGroup>(R.id.layout)
		this.rootView = layoutInflater.inflate(this.layoutId, layout)
		
		return view
	}
	
	override fun onViewCreated(
		view: View,
		savedInstanceState: Bundle?
	)
	{
		super.onViewCreated(view, savedInstanceState)
		
		this.onLayoutCreatedListener?.invoke(this.rootView)
		
		val activity = this.activity!!
		
		toolbar.navigationIcon = IconicsDrawable(activity)
			.icon(FontAwesome.Icon.faw_arrow_left)
			.sizeRes(R.dimen.app_bar_icon_size)
			.colorRes(R.color.backgroundPrimary)
		
		toolbar.inflateMenu(R.menu.dialog_fullscreen)
		val menu = toolbar.menu
		this.action = menu.findItem(R.id.action)
		this.action.icon = IconicsDrawable(activity)
			.icon(FontAwesome.Icon.faw_check)
			.sizeRes(R.dimen.app_bar_icon_size)
			.colorRes(R.color.backgroundPrimary)
		
		toolbar.setNavigationOnClickListener { this.dismiss() }
		toolbar.title = this.title
		toolbar.setOnMenuItemClickListener {
			this.action.setActionView(R.layout.dialog_form_loader)
			setRootViewState(false)
			GlobalScope.launch(Dispatchers.IO) {
				onLayoutUpdatedListener?.invoke(rootView)
			}
			true
		}
	}
	
	private fun setRootViewState(state: Boolean)
	{
		var depth = 0
		val views: MutableMap<Int, MutableList<ViewGroup>> = mutableMapOf()
		views[depth] = mutableListOf(this.rootView as ViewGroup)
		
		while(true)
		{
			val viewGroups = views[depth]
			val currentViewGroup = mutableListOf<ViewGroup>()
			
			for(i in 0 until viewGroups!!.size)
			{
				val viewGroup = viewGroups[i]
				
				for(j in 0 until viewGroup.childCount)
				{
					val children = viewGroup[j]
					
					if(children is ViewGroup)
					{
						currentViewGroup.add(children)
					}
				}
			}
			
			if(currentViewGroup.size == 0)
			{
				break
			}
			
			depth++
			
			views[depth] = currentViewGroup
		}
		
		views.flatMap {
			it.value
		}.forEach {
			it.isEnabled = state
		}
	}
	
	fun unvalidate()
	{
		action.actionView = null
		setRootViewState(true)
	}
	
	fun validate()
	{
		this.dismiss()
	}
}