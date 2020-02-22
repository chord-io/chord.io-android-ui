package io.chord.ui.behaviors

import io.chord.clients.models.ThemeEntry
import java.util.Collections.max

class BarBehavior
{
	lateinit var onCount: (() -> List<ThemeEntry>)
	
	fun count(): Int
	{
		val themes = this.onCount()
		val bars = themes.map { theme ->
			theme.length.end.toInt()
		}
		.distinct()
		
		if(bars.isEmpty())
		{
			return 0
		}
		
		return max(bars)
	}
}