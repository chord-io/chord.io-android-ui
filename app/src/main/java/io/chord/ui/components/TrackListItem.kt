package io.chord.ui.components

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import io.chord.R
import io.chord.databinding.TrackListItemBinding
import io.chord.ui.models.TrackListItemViewModel
import io.chord.ui.utils.ViewUtils

class TrackListItem(
	val view: View
)
{
	private lateinit var binding: TrackListItemBinding
	private var initialNameViewTextSize: Float = 0f
	
	fun bind(model: TrackListItemViewModel)
	{
		this.binding = DataBindingUtil.bind(this.view)!!
		this.binding.track = model
		
		this.initialNameViewTextSize = this.binding.name.textSize
		
//		this.binding.name
//			.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
//				val textView = this.binding.name
//				val textSize = ViewUtils.getMaximumTextSize(
//					textView.text.toString(),
//					this.initialNameViewTextSize,
//					textView.height.toFloat(),
//					textView.paint
//				)
//				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
//
//				val rect = Rect()
//				textView.paint.getTextBounds(textView.text.toString(), 0, textView.text.length, rect)
//				Log.i("FFF", ViewUtils.pixelToSp(textSize).toString())
//				Log.i("FFF", ViewUtils.pixelToDp(textView.height.toFloat()).toString())
//				Log.i("FFF", ViewUtils.pixelToDp((rect.bottom - rect.top).toFloat()).toString())
//			}
	}
}