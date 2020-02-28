package io.chord.ui.extensions

@UseExperimental(ExperimentalUnsignedTypes::class)
fun Int.toHexaDecimalString(): String
{
	return this.toUInt().toString(16)
}