package io.chord.ui.components

import io.chord.ui.behaviors.Bindable
import io.chord.ui.utils.QuantizeUtils

interface Quantifiable : Bindable
{
	fun setQuantization(quantization: QuantizeUtils.Quantization)
}