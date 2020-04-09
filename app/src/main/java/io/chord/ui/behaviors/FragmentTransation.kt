package io.chord.ui.behaviors

import androidx.fragment.app.FragmentManager

interface FragmentTransation
{
	fun load(manager: FragmentManager)
	fun unload(manager: FragmentManager)
}