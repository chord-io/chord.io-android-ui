package io.chord.ui.animations

import android.animation.ValueAnimator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class FastOutSlowInValueAnimator : ValueAnimator()
{
	init
	{
		this.interpolator = FastOutSlowInInterpolator()
	}
}