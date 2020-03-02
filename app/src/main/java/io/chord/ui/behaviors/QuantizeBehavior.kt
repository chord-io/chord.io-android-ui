package io.chord.ui.behaviors

import io.chord.ui.utils.QuantizeUtils

class QuantizeBehavior
{
	private var _offset: Float = 0f
	private var _segmentCount: Int = 0
	private var _segmentLength: Float = 0f
	private var _quantization: QuantizeUtils.Quantization = QuantizeUtils.Quantization(
		QuantizeUtils.QuantizeValue.Fourth,
		QuantizeUtils.QuantizeMode.Natural
	)
	private lateinit var _points: List<List<Float>>
	
	var offset: Float
		get() = this._offset
		set(value) {
			this._offset = value
		}
	
	var segmentCount: Int
		get() = this._segmentCount
		set(value) {
			this._segmentCount = value
		}
	
	var segmentLength: Float
		get() = this._segmentLength
		set(value) {
			this._segmentLength = value
		}
	
	var quantization: QuantizeUtils.Quantization
		get() = this._quantization
		set(value) {
			this._quantization = value
		}
	
	val points: List<List<Float>>
		get() = this._points
	
	fun generate()
	{
		val points = mutableListOf<List<Float>>()
		
		for(index in 0 until this.segmentCount)
		{
			points.add(this.generateSegement(index))
		}
		
		this._points = points.toList()
	}
	
	private fun generateSegement(index: Int): List<Float>
	{
		val left = this.segmentLength * index
		val right = left + this.segmentLength
		val width = right - left
		var count = this.quantization.count
		val points = mutableListOf<Float>()
		
		if(this.quantization.mode == QuantizeUtils.QuantizeMode.Dotted)
		{
			count -= 1
		}
		
		for(i in 0 until count)
		{
			val x = width * (i * this.quantization.value) + this.offset
			points.add(left + x)
		}
		
		return points
	}
}