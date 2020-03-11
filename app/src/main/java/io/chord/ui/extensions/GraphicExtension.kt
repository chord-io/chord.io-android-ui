package io.chord.ui.extensions

import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
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

fun String.alignCenter(
	x: Int,
	y: Int,
	painter: Paint
): PointF
{
	val newX = x - painter.measureText(this) / 2f
	val newY = y - (painter.descent() + painter.ascent()) / 2f
	return PointF(newX, newY)
}

fun String.alignRight(
	x: Int,
	y: Int,
	painter: Paint
): PointF
{
	val bounds = this.getTextBounds(painter)
	val newX = x - bounds.right.toFloat()
	val newY = y - bounds.bottom.toFloat()
	return PointF(newX, newY)
}

fun String.findOptimalTextSize(
	textSize: Float,
	constraint: Float,
	boundsEvaluator: ((Rect) -> Int),
	painter: Paint
): Float
{
	if(constraint <= 0f || this.isBlank() || this.isEmpty())
	{
		return 0f
	}
	
	val resolver = LocalOptimumResolver(textSize, 0.001f)
	val bounds = Rect()
	val clonedPainter = Paint(painter)
	
	return resolver.resolve { value ->
		clonedPainter.textSize = value
		clonedPainter.getTextBounds(this, 0, this.length, bounds)
		val edge = boundsEvaluator(bounds)
		var spSize = value.pixelToSp()
		
		if(edge > constraint)
		{
			spSize -= 1
		}
		else if(edge <= constraint)
		{
			spSize += 1
		}
		
		spSize.spToPixel()
	}
}

// TODO remove this
fun String.findMaximumTextSize(
	textSize: Float,
	constraint: Float,
	boundsEvaluator: ((Rect) -> Int),
	painter: Paint
): Float
{
	val resolver = LocalMaximumResolver(textSize, 0.001f)
	val bounds = Rect()
	val clonedPainter = Paint(painter)
	
	return resolver.resolve { value ->
		clonedPainter.textSize = value
		clonedPainter.getTextBounds(this, 0, this.length, bounds)
		val edge = boundsEvaluator(bounds)
		var spSize = value.pixelToSp()
		
		if(edge > constraint)
		{
			spSize -= 1
		}
		
		spSize.spToPixel()
	}
}