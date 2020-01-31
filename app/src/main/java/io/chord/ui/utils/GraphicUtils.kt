package io.chord.ui.utils

import android.graphics.Path
import android.graphics.RectF

class GraphicUtils
{
	companion object
	{
		// TODO: transfert method from ViewUtils to this class
		
		fun getRoundRectPath(
			rect: RectF,
			topLeft: Float,
			topRight: Float,
			bottomRight: Float,
			bottomLeft: Float
		): Path
		{
			val corners = floatArrayOf(
				topLeft, topLeft,
				topRight, topRight,
				bottomRight, bottomRight,
				bottomLeft, bottomLeft
			)
			
			val path = Path()
			path.addRoundRect(rect, corners, Path.Direction.CW)
			return path
		}
	}
}