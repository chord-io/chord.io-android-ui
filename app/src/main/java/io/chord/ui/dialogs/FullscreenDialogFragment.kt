package io.chord.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorRes
import com.mikepenz.iconics.utils.sizeRes
import io.chord.R
import io.chord.ui.components.Banner
import io.chord.ui.utils.ViewUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


open class FullscreenDialogFragment<TBinding: ViewDataBinding>(
	private val layoutId: Int,
	protected var title: String? = null
) : DialogFragment()
{
	private lateinit var dataBinding: TBinding
	private lateinit var toolbar: Toolbar
	private lateinit var rootView: View
	private lateinit var action: MenuItem
	lateinit var banner: Banner
	
	var onLayoutCreatedListener: ((dataBinding: TBinding) -> Unit)? = null
	var onLayoutUpdatedListener: ((dataBinding: TBinding) -> Unit)? = null
	var onViewModelBinding: ((dataBinding: TBinding) -> Unit)? = null
	
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
		
		val view = inflater.inflate(R.layout.dialog_fullscreen, container)
		this.toolbar = view.findViewById(R.id.toolbar)
		this.banner = view.findViewById(R.id.banner)
		
		val layout = view.findViewById<ViewGroup>(R.id.frameLayout)
//		this.rootView = inflater.inflate(this.layoutId, layout)
//		val children = ViewUtils.getDirectChildren(this.rootView)!!.first()
//		this.dataBinding = DataBindingUtil.bind(children)!!
//
//		this.onViewModelBinding?.invoke(this.dataBinding)
		
		this.dataBinding = DataBindingUtil.inflate(
			inflater,
			this.layoutId,
			layout,
			true
		)
		this.onViewModelBinding?.invoke(this.dataBinding)
		this.rootView = this.dataBinding.root
		
		return view
	}
	
	override fun onViewCreated(
		view: View,
		savedInstanceState: Bundle?
	)
	{
		super.onViewCreated(view, savedInstanceState)
		
		this.onLayoutCreatedListener?.invoke(this.dataBinding)
		
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
			ViewUtils.setViewState(this.rootView as ViewGroup, false)
			GlobalScope.launch(Dispatchers.IO) {
				onLayoutUpdatedListener?.invoke(dataBinding)
			}
			true
		}
	}
	
	fun unvalidate()
	{
		this.action.actionView = null
		ViewUtils.setViewState(this.rootView as ViewGroup, true)
	}
	
	fun validate()
	{
		this.dismiss()
	}
}