package io.chord.ui.fragments.theme

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import androidx.databinding.DataBindingUtil
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import com.mikepenz.iconics.utils.colorInt
import io.chord.databinding.ThemeListHeaderBinding
import io.chord.ui.extensions.dpToPixel
import io.chord.ui.extensions.toTransparent
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.HeaderViewHolder
import io.chord.ui.sections.Section

class ThemeHeaderViewHolder(
    view: View
) : HeaderViewHolder<ThemeSectionItem, ThemeViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ThemeListHeaderBinding>(this.itemView)!!
    
    override fun bind(
        title: String,
        section: Section<ThemeSectionItem, ThemeViewHolder>,
        clickListener: ClickListener<ThemeSectionItem, ThemeViewHolder>
    )
    {
        this.bind(
            title,
            section as ThemeSection,
            clickListener as ThemeClickListener
        )
    }
    
    private fun bind(
        title: String,
        section: ThemeSection,
        clickListener: ThemeClickListener
    )
    {
        this.initialise(section)
        this.binding.title.text = title
        this.binding.counter.text = section.getRealContentItemsTotal().toString()
        this.binding.layout.setOnClickListener {
            section.toggle()
            this.toggle(section.isExpanded)
        }
    }
    
    private fun initialise(section: ThemeSection)
    {
        this.binding.icon.icon!!.colorInt(section.track.color)
        this.toggle(section.isExpanded)
        val background = this.binding.layout.background as LayerDrawable
        val drawable = background.getDrawable(0) as GradientDrawable
        drawable.setColor(section.track.color.toTransparent(0.1f))
        drawable.setStroke(2f.dpToPixel().toInt(), section.track.color.toTransparent(0.2f))
    }
    
    private fun toggle(isExpanded: Boolean)
    {
        val icon = if(isExpanded)
        {
            FontAwesome.Icon.faw_caret_down
        }
        else
        {
            FontAwesome.Icon.faw_caret_right
        }
        this.binding.icon.icon!!.icon(icon)
    }
}