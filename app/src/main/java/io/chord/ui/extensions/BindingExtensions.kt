package io.chord.ui.extensions

import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

@BindingAdapter("android:text")
fun setProgress(view: EditText, value: Int)
{
	view.setText(value.toString())
}

@BindingAdapter("android:text")
fun setProgress(view: TextView, value: Int)
{
	view.text = value.toString()
}

@InverseBindingAdapter(attribute = "android:text")
fun getProgress(view: EditText): Int
{
	return view.text.toString().toInt()
}

