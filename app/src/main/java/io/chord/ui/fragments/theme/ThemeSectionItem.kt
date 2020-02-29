package io.chord.ui.fragments.theme

import io.chord.clients.models.Theme
import io.chord.clients.models.Track

class ThemeSectionItem(
	val tracks: List<Track>,
	val track: Track,
	val theme: Theme
)
{
	override fun equals(other: Any?): Boolean
	{
		if(other is ThemeSectionItem)
		{
			return this.theme.name == other.theme.name
		}
		
		return false
	}
	
	override fun hashCode(): Int
	{
		return super.hashCode()
	}
}