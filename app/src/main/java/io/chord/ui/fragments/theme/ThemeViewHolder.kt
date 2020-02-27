package io.chord.ui.fragments.theme

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorInt
import io.chord.clients.models.Theme
import io.chord.clients.models.Track
import io.chord.databinding.ThemeListItemBinding
import io.chord.ui.extensions.toTransparent
import io.chord.ui.models.ThemeListItemViewModel
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ViewHolderBase
import io.chord.ui.utils.RippleDrawableUtils

class ThemeViewHolder(
    view: View
) : ViewHolderBase<ThemeSectionItem, ThemeViewHolder>(view)
{
    private companion object
    {
        val holders: MutableList<ThemeViewHolder> = mutableListOf()
        val states: MutableMap<String, Boolean> = mutableMapOf()
    }
    
    private lateinit var tracks: List<Track>
    private lateinit var track: Track
    private lateinit var theme: Theme
    val binding = DataBindingUtil.bind<ThemeListItemBinding>(this.itemView)!!
    
    private var isPlaying: Boolean
        get() = this.binding.theme!!.isPlaying
        set(value) {
            this.binding.theme!!.isPlaying = value
            states[this.getStateKey()] = value
            this.setControlIcon(value)
        }
    
    init
    {
        holders.add(this)
    
        this.binding.theme = ThemeListItemViewModel(false)
    }
    
    private fun getStateKey(): String
    {
        return "${this.track.name}-${this.theme.name}"
    }
    
    private fun getStateValue(): Boolean
    {
        states.filterKeys { key ->
            val components = key.split('-')
            val trackKey = components[0]
            val themeKey = components[1]
            
            val isTrackNotExist = !this.tracks.any {
                trackKey == it.name
            }
            val isThemeNotExist = !this.tracks.any { track ->
                track.themes.any {
                    themeKey == it.name
                }
            }
            isTrackNotExist || isThemeNotExist
        }
        .forEach {
            states.remove(it.key)
        }
    
        val stateKey = this.getStateKey()
        return if(states.containsKey(stateKey))
        {
            states[stateKey]!!
        }
        else
        {
            states[stateKey] = false
            states[stateKey]!!
        }
    }
    
    override fun bind(item: ThemeSectionItem, clickListener: ClickListener<ThemeSectionItem, ThemeViewHolder>)
    {
        this.tracks = item.tracks
        this.track = item.track
        this.theme = item.theme
        
        val state = this.getStateValue()
        
        this.binding.icon.icon!!.colorInt(this.track.color)
        
        this.binding.icon.icon!!
            .icon(FontAwesome.Icon.faw_play)
    
        this.binding.theme!!.fromModel(this.theme)
        this.setControlIcon(state)
        
        this.binding.layout.isClickable = true
        this.binding.layout.isFocusable = true
        
        val shape = RectShape()
        val mask = ShapeDrawable(shape)
        
        this.binding.layout.background = RippleDrawableUtils.create(
            this.track.color.toTransparent(0.1f),
            this.track.color,
            mask
        )
        
        val themeClickListener = clickListener as ThemeClickListener<ThemeSectionItem, ThemeViewHolder>
        
        this.binding.icon.setOnClickListener {
            themeClickListener.onPlayClicked(item)
            holders.forEach { holder ->
                holder.isPlaying = false
            }
            this.isPlaying = true
        }
        
        this.itemView.setOnClickListener {
            clickListener.onItemClicked(item, this)
        }

        this.itemView.setOnLongClickListener {
            clickListener.onItemLongClicked(item, this)
        }
    }
    
    private fun setControlIcon(state: Boolean)
    {
        if(state)
        {
            this.binding.icon.icon!!.icon(FontAwesome.Icon.faw_stop)
        }
        else
        {
            this.binding.icon.icon!!.icon(FontAwesome.Icon.faw_play)
        }
    }
}