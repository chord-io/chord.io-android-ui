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
			val trackEqual = this.track.color == other.track.color
			val themeEqual = this.theme.name == other.theme.name
			return trackEqual && themeEqual
		}
		
		return false
	}
	
	override fun hashCode(): Int
	{
		return super.hashCode()
	}
}