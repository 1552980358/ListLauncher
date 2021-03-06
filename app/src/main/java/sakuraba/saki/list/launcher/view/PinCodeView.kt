package sakuraba.saki.list.launcher.view

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
    
    private var centerXStart = 0F
    
    private var centerY = 0F
    
    private var diffX = 0F
    
    fun getInput() = inputSize
    
    fun update(newInputSize: Int) {
        inputSize = newInputSize
        postInvalidate()
    }
    
    fun getMaxInputSize() = maxInputSize
    
    fun setMaxInputSize(newMaxInputSize: Int) {
        maxInputSize = newMaxInputSize
        postInvalidate()
    }
    
    fun updateRadius(newRadius: Float) {
        radius = newRadius
        diffX = radius * 2 + margin
        centerXStart = width / 2F - (maxInputSize / 2 - 1) * diffX - radius - margin / 2F
        postInvalidate()
    }
    
    fun updateMargin(newMargin: Float) {
        margin = newMargin
        diffX = radius * 2 + margin
        centerXStart = width / 2F - (maxInputSize / 2 - 1) * diffX - radius - margin / 2F
        postInvalidate()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerY = MeasureSpec.getSize(heightMeasureSpec) / 2F
        diffX = radius * 2 + margin
        centerXStart = MeasureSpec.getSize(widthMeasureSpec) / 2F - (maxInputSize / 2 - 1) * diffX - radius - margin / 2F
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        
        canvas?:return
    
        var centerX = centerXStart
    
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