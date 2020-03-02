package io.chord.ui.behaviors

import io.chord.clients.models.ThemeEntry
import java.util.Collections.max

class BarBehavior
{
	var headRoom: UInt = 1U
	lateinit var onCount: (() -> List<ThemeEntry>)
	
	fun count(): Int
	{
		val themes = this.onCount()
		val bars = themes.map { theme ->
			theme.length.end.toInt()
		}
		.distinct()
		
		if(this.headRoom == 0U)
		{
			this.headRoom = 1U
		}
		
		if(bars.isEmpty())
		{
			return this.headRoom.toInt()
		}
		
		return max(bars) + this.headRoom.toInt()
	}
}