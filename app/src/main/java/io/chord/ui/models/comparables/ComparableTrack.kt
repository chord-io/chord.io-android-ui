package io.chord.ui.models.comparables

import io.chord.clients.models.Track

fun Track.toComparable(): ComparableTrack
{
	return ComparableTrack(this)
}

fun List<Track>.toComparable(): List<ComparableTrack>
{
	return this.map {
		it.toComparable()
	}
}

class ComparableTrack(
	private val track: Track
)
{
	override fun equals(other: Any?): Boolean
	{
		if(other is ComparableTrack)
		{
			val isEqualThemes = this.track.themes.toComparable() == other.track.themes.toComparable()
			return this.track.referenceId == other.track.referenceId && isEqualThemes
		}
		
		return false
	}
	
	override fun hashCode(): Int
	{
		return this.track.referenceId
	}
}
