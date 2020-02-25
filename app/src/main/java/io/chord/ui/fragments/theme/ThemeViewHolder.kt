package io.chord.ui.fragments.theme

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorInt
import io.chord.R
import io.chord.clients.models.Theme
import io.chord.databinding.ThemeListItemBinding
import io.chord.ui.models.ThemeListItemViewModel
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ViewHolderBase
import io.chord.ui.utils.RippleDrawableUtils

class ThemeViewHolder(
    private val color: Int,
    view: View
) : ViewHolderBase<Theme, ThemeViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ThemeListItemBinding>(this.itemView)!!
    private var _isPlaying = false
    
    var isPlaying: Boolean
        get() = this._isPlaying
        set(value) {
            this._isPlaying = value
            if(this._isPlaying)
            {
                this.binding.icon.icon!!.icon(FontAwesome.Icon.faw_stop)
            }
            else
            {
                this.binding.icon.icon!!.icon(FontAwesome.Icon.faw_play)
            }
        }
    
    init
    {
    	this.binding.icon.icon!!
            .colorInt(this.color)
    }
    
    override fun bind(item: Theme, clickListener: ClickListener<Theme, ThemeViewHolder>)
    {
        this.binding.icon.icon!!
            .icon(FontAwesome.Icon.faw_play)
        
        this.binding.theme = ThemeListItemViewModel(item)
        
        this.binding.layout.isClickable = true
        this.binding.layout.isFocusable = true
        
        val shape = RectShape()
        val mask = ShapeDrawable(shape)
        
        this.binding.layout.background = RippleDrawableUtils.create(
            this.itemView.resources.getColor(R.color.backgroundTernary, this.itemView.context.theme),
            this.color,
            mask
        )
        
        val themeClickListener = clickListener as ThemeClickListener<Theme, ThemeViewHolder>
        
        this.binding.icon.setOnClickListener {
            themeClickListener.onPlayClicked(item)
            this.isPlaying = true
        }
        
        this.itemView.setOnClickListener {
            clickListener.onItemClicked(item, this)
        }

        this.itemView.setOnLongClickListener {
            clickListener.onItemLongClicked(item, this)
        }
    }
}