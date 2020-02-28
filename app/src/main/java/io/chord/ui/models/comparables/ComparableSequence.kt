package io.chord.ui.models.comparables

import io.chord.clients.models.Sequence

fun Sequence.toComparable(): ComparableSequence
{
	return ComparableSequence(this)
}

fun List<Sequence>.toComparable(): List<ComparableSequence>
{
	return this.map {
		it.toComparable()
	}
}

class ComparableSequence(
	private val sequence: Sequence
)
{
	override fun equals(other: Any?): Boolean
	{
		if(other is ComparableSequence)
		{
			return this.sequence.referenceId == other.sequence.referenceId
		}
		
		return false
	}
	
	override fun hashCode(): Int
	{
		return this.sequence.referenceId
	}
}