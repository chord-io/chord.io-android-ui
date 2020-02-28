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
import io.chord.ui.sections.ExpandableSection
import io.chord.ui.sections.HeaderViewHolder
import io.chord.ui.sections.Section

class ThemeHeaderViewHolder(
    private val color: Int,
    isExpanded: Boolean,
    view: View
) : HeaderViewHolder<ThemeSectionItem, ThemeViewHolder>(view)
{
    val binding = DataBindingUtil.bind<ThemeListHeaderBinding>(this.itemView)!!
    
    init
    {
        this.binding.icon.icon!!
            .colorInt(this.color)
        this.toggle(isExpanded)
        val background = this.binding.layout.background as LayerDrawable
        val drawable = background.getDrawable(0) as GradientDrawable
        drawable.setColor(this.color.toTransparent(0.1f))
        drawable.setStroke(2f.dpToPixel().toInt(), this.color.toTransparent(0.2f))
    }
    
    override fun bind(
        item: ThemeSectionItem,
        clickListener: ClickListener<ThemeSectionItem, ThemeViewHolder>
    )
    {
        throw NotImplementedError()
    }
    
    override fun bind(
        title: String,
        section: Section<ThemeSectionItem, ThemeViewHolder>,
        clickListener: ClickListener<ThemeSectionItem, ThemeViewHolder>
    )
    {
        val expandableSection = section as ExpandableSection
        this.binding.title.text = title
        this.binding.counter.text = expandableSection.getRealContentItemsTotal().toString()
        this.binding.layout.setOnClickListener {
            expandableSection.toggle()
            this.toggle(expandableSection.isExpanded)
        }
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