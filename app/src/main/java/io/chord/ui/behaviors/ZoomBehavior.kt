package io.chord.ui.behaviors

import io.chord.ui.animations.FastOutSlowInValueAnimator

class ZoomBehavior
{
	private var _factorWidth: Float = 0f
	private var _factorHeight: Float = 0f
	private var _factorizedWidth: Float = 0f
	private var _factorizedHeight: Float = 0f
	
	val widthAnimator: FastOutSlowInValueAnimator = FastOutSlowInValueAnimator()
	val heightAnimator: FastOutSlowInValueAnimator = FastOutSlowInValueAnimator()
	lateinit var onEvaluateWidth: (() -> Float)
	lateinit var onEvaluateHeight: (() -> Float)
	lateinit var onMeasureWidth: (() -> Unit)
	lateinit var onMeasureHeight: (() -> Unit)
	
	init
	{
		this.widthAnimator.addUpdateListener {
			val value = it.animatedValue as Float
			this._factorWidth = value
			this.measureWidth()
		}
		
		this.heightAnimator.addUpdateListener {
			val value = it.animatedValue as Float
			this._factorHeight = value
			this.measureHeight()
		}
	}
	
	private fun measureWidth()
	{
		this._factorizedWidth = this.onEvaluateWidth() * this._factorWidth
		this.onMeasureWidth()
	}
	
	private fun measureHeight()
	{
		this._factorizedHeight = this.onEvaluateHeight() * this._factorHeight
		this.onMeasureHeight()
	}
	
	fun requestMeasure()
	{
		this.measureWidth()
		this.measureHeight()
	}
	
	fun getFactorWidth(): Float
	{
		return this._factorWidth
	}
	
	fun setFactorWidth(factor: Float, animate: Boolean)
	{
		if(animate)
		{
			this.widthAnimator.cancel()
			this.widthAnimator.setFloatValues(this._factorWidth, factor)
			this.widthAnimator.start()
		}
		else
		{
			this._factorWidth = factor
			this.measureWidth()
		}
	}
	
	fun getFactorHeight(): Float
	{
		return this._factorHeight
	}
	
	fun setFactorHeight(factor: Float, animate: Boolean)
	{
		if(animate)
		{
			this.heightAnimator.cancel()
			this.heightAnimator.setFloatValues(this._factorHeight, factor)
			this.heightAnimator.start()
		}
		else
		{
			this._factorHeight = factor
			this.measureHeight()
		}
	}
	
	val factorizedWidth: Float
		get() = this._factorizedWidth
	
	val factorizedHeight: Float
		get() = this._factorizedHeight
}