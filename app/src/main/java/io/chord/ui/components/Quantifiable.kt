package io.chord.ui.components

import io.chord.ui.utils.QuantizeUtils

interface Quantifiable
{
	fun setQuantization(quantization: QuantizeUtils.Quantization)
}