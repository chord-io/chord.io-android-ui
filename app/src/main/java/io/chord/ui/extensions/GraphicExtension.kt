package io.chord.ui.extensions

import android.graphics.*
import android.util.TypedValue
import io.chord.ui.ChordIOApplication
import io.chord.ui.maths.LocalMaximumResolver
import io.chord.ui.maths.LocalOptimumResolver

fun RectF.round(
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
	path.addRoundRect(this, corners, Path.Direction.CW)
	return path
}

fun Float.dpToPixel(): Float
{
	val metrics = ChordIOApplication.instance.resources.displayMetrics
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, metrics)
}

fun Float.pixelToDp(): Float
{
	val metrics = ChordIOApplication.instance.resources.displayMetrics
	return this / metrics.density
}

fun Float.spToPixel(): Float
{
	val metrics = ChordIOApplication.instance.resources.displayMetrics
	return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, metrics)
}

fun Float.pixelToSp(): Float
{
	val metrics = ChordIOApplication.instance.resources.displayMetrics
	return this / metrics.scaledDensity
}

fun String.getTextBounds(painter: Paint): Rect
{
	val bounds = Rect()
	painter.getTextBounds(this, 0, this.count(), bounds)
	return bounds
}

fun String.getTextCentered(
	x: Int,
	y: Int,
	painter: Paint
): PointF
{
	val newX = x - painter.measureText(this) / 2f
	val newY = y - (painter.descent() + painter.ascent()) / 2f
	return PointF(newX, newY)
}

fun String.getOptimalTextSize(
	textSize: Float,
	height: Float,
	painter: Paint
): Float
{
	val resolver = LocalOptimumResolver(textSize, 0.001f)
	val bounds = Rect()
	val clonedPainter = Paint(painter)
	
	return resolver.resolve { value ->
		clonedPainter.textSize = value
		clonedPainter.getTextBounds(this, 0, this.length, bounds)
		
		var spSize = value.pixelToSp()
		
		if(bounds.height() > height)
		{
			spSize -= 1
		}
		else if(bounds.height() <= height)
		{
			spSize += 1
		}
		
		spSize.spToPixel()
	}
}

fun String.getMaximumTextSize(
	textSize: Float,
	height: Float,
	painter: Paint
): Float
{
	val resolver = LocalMaximumResolver(textSize, 0.001f)
	val bounds = Rect()
	val clonedPainter = Paint(painter)
	
	return resolver.resolve { value ->
		clonedPainter.textSize = value
		clonedPainter.getTextBounds(this, 0, this.length, bounds)
		
		var spSize = value.pixelToSp()
		
		if(bounds.height() > height)
		{
			spSize -= 1
		}
		
		spSize.spToPixel()
	}
}