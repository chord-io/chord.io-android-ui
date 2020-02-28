package io.chord.ui.models.comparables

import io.chord.clients.models.Theme

fun Theme.toComparable(): ComparableTheme
{
	return ComparableTheme(this)
}

fun List<Theme>.toComparable(): List<ComparableTheme>
{
	return this.map {
		it.toComparable()
	}
}

class ComparableTheme(
	private val theme: Theme
)
{
	override fun equals(other: Any?): Boolean
	{
		if(other is ComparableTheme)
		{
			val isEqualSequences = this.theme.sequences.toComparable() == other.theme.sequences.toComparable()
			return this.theme.referenceId == other.theme.referenceId && isEqualSequences
		}
		
		return false
	}
	
	override fun hashCode(): Int
	{
		return this.theme.referenceId
	}
}