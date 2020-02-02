package io.chord.ui.behaviors

import io.chord.ui.utils.MathUtils

class StepPositionBehavior
{
	private var position: Float = 0f
	private var index: Int = 0
	
	lateinit var onMeasureSteps: (() -> List<Float>)
	lateinit var onPositionChanged: ((Float, Int) -> Unit)
	
	fun getPosition(): Float
	{
		return this.position
	}
	
	fun setPosition(position: Float)
	{
		val steps = this.onMeasureSteps()
		val step = MathUtils.nearest(position, steps)
		this.index = steps.indexOf(step)
		this.position = step
		this.onPositionChanged(this.position, this.index)
	}
	
	fun getIndex(): Int
	{
		return this.index
	}
	
	fun setIndex(index: Int)
	{
		val steps = this.onMeasureSteps()
		val step = steps[index]
		this.index = index
		this.position = step
		this.onPositionChanged(this.position, this.index)
	}
}