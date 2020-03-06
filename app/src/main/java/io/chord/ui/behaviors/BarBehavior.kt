package io.chord.ui.behaviors

import java.util.Collections.max

class BarBehavior
{
	var headRoom: UInt = 1U
	lateinit var onCount: (() -> List<Int>)
	
	fun count(): Int
	{
		val bars = this.onCount().distinct()
		
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