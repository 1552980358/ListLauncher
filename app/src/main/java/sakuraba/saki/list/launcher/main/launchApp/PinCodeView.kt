package sakuraba.saki.list.launcher.main.launchApp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import sakuraba.saki.list.launcher.R

class PinCodeView: View {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private val paint = Paint().apply {
        isAntiAlias = true
    }
    
    private var inputSize = 0
    
    private var maxInputSize = 4
    
    private var radius = resources.getDimension(R.dimen.view_pin_code_radius)
    
    private var margin = resources.getDimension(R.dimen.view_pin_code_circle_margin)
    
    fun update(newInputSize: Int) {
        inputSize = newInputSize
        postInvalidate()
    }
    
    fun setMaxInputSize(newMaxInputSize: Int) {
        maxInputSize = newMaxInputSize
        postInvalidate()
    }
    
    fun updateRadius(newRadius: Float) {
        radius = newRadius
        postInvalidate()
    }
    
    fun updateMargin(newMargin: Float) {
        margin = newMargin
        postInvalidate()
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        canvas?:return
    
        var centerX = width / 2F - radius * 3 + margin
        val centerY = height / 2F
    
        val diffX = radius * 2 + margin
    
        paint.style = Paint.Style.FILL
        repeat(inputSize) {
            canvas.drawCircle(centerX, centerY, radius, paint)
            centerX += diffX
        }
    
        paint.style = Paint.Style.STROKE
        repeat(maxInputSize - inputSize) {
            canvas.drawCircle(centerX, centerY, radius, paint)
            centerX += diffX
        }
        
    }
    
}