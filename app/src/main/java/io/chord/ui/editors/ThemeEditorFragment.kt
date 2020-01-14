package io.chord.ui.editors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.chord.R

class ThemeEditorFragment : Fragment()
{
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View?
	{
		return inflater.inflate(R.layout.theme_editor, container, false)
	}
}
