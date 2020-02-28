package io.chord.ui.fragments.theme

import io.chord.R
import io.chord.clients.models.Track
import io.chord.ui.sections.ClickListener
import io.chord.ui.sections.ExpandableSection

class ThemeSection : ExpandableSection<ThemeSectionItem, ThemeViewHolder>
{
	private var _track: Track
	
	var track: Track
		get() = this._track
		set(value) {
			this._track = value
			this.title = value.name
			this.adapter.notifyHeaderChanged()
		}
	
	constructor(
		track: Track,
		isExpanded: Boolean,
		clickListener: ClickListener<ThemeSectionItem, ThemeViewHolder>
	): super(
		track.name,
		R.layout.theme_list_item,
		R.layout.theme_list_header,
		R.layout.section_mini_empty,
		R.layout.section_mini_loading,
		R.layout.section_mini_failed,
		{ view -> ThemeViewHolder(view) },
		{ view -> ThemeHeaderViewHolder(view) },
		clickListener
	)
	{
		this.isExpanded = isExpanded
		this._track = track
	}
}