package io.chord.ui.components

import io.chord.ui.behaviors.Bindable

interface Zoomable : Bindable
{
	fun setZoomFactor(orientation: ViewOrientation, factor: Float, animate: Boolean = true)
}