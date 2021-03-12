package ir.mrahimy.conceal.util.cv

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ir.mrahimy.conceal.R

/**
 * Simple view that shows a frame with transparent bg
 *
 * @property mLineColor: providing the color in xml, defaults to black
 * @property borderWidth: defines the line width to be drawn
 * @property lineLength: the length of lines defaults to 50
 */
class CameraCorner(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var borderWidth = 4.0f
    private var lineLength = 50f
    private var mLineColor = Color.WHITE

    private val linesPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = mLineColor
        strokeWidth = borderWidth
    }

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CameraCorner, 0, 0)

        typedArray.apply {
            mLineColor = getColor(R.styleable.CameraCorner_lineColor, mLineColor)
        }
        linesPaint.color = mLineColor
        typedArray.recycle()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        drawCameraCorner(canvas)
    }

    private fun drawCameraCorner(canvas: Canvas?) {

        val startX = paddingStart.toFloat()
        val startY = paddingTop.toFloat()
        val endX = width - paddingEnd.toFloat()
        val endY = height - paddingBottom.toFloat()

        canvas?.drawLine(startX, startY, startX + lineLength, startY, linesPaint)
        canvas?.drawLine(startX, startY, startX, startY + lineLength, linesPaint)

        canvas?.drawLine(startX, endY, startX + lineLength, endY, linesPaint)
        canvas?.drawLine(startX, endY, startX, endY - lineLength, linesPaint)

        canvas?.drawLine(endX, startY, endX - lineLength, startY, linesPaint)
        canvas?.drawLine(endX, startY, endX, startY + lineLength, linesPaint)

        canvas?.drawLine(endX, endY, endX - lineLength, endY, linesPaint)
        canvas?.drawLine(endX, endY, endX, endY - lineLength, linesPaint)

    }
}