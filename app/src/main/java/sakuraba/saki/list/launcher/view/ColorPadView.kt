package sakuraba.saki.list.launcher.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import lib.github1552980358.ktExtension.android.view.heightF
import lib.github1552980358.ktExtension.android.view.widthF
import sakuraba.saki.list.launcher.view.base.BaseArrowView

@SuppressLint("ClickableViewAccessibility")
class ColorPadView: BaseArrowView {
    
    companion object {
        fun interface OnHSEChangeListener {
            fun onHSEChange(hse: Float)
        }
    }
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private val colorPadData = arrayOf(
        "#EF3D1F", "#EF3E1F", "#EE3D1F", "#EF3E1F", "#EE3D1E", "#EF3E1F", "#EF3E25", "#EF3E2A",
        "#EF3E31", "#EF3F37", "#EE3E3C", "#EF3F43", "#EE3F48", "#EE404E", "#EE4054", "#EE415A",
        "#EE4260", "#ED4266", "#ED436B", "#EE4473", "#EE4578", "#ED467E", "#ED4684", "#ED478A",
        "#ED4890", "#ED4A96", "#ED4B9C", "#ED4CA2", "#EC4DA8", "#EC4DAE", "#ED4FB5", "#EC50BA",
        "#EC51C0", "#EB52C6", "#EC54CC", "#EB56D3", "#EB57D9", "#EA57DE", "#EB59E4", "#EA5BEB",
        "#EA5DF1", "#E95DF6", "#E95EFA", "#E95EFA", "#EA5FFA", "#EA5FFA", "#E95EFA", "#E85FFB",
        "#E15DFA", "#DC5CFA", "#D55BFA", "#D05AFA", "#CA5AFA", "#C358F9", "#BD57F9", "#B857FA",
        "#B256FA", "#AC55FA", "#A654F9", "#A054F9", "#9B53F9", "#9552F9", "#8F52F9", "#8850F9",
        "#8450F9", "#7E50F9", "#784FF9", "#714EF8", "#6D4EF9", "#674EF9", "#624EF9", "#5C4DF9",
        "#574DF9", "#524DF9", "#4C4CF9", "#474CF9", "#434CF9", "#3E4CF9", "#3A4BF9", "#354BF9",
        "#314BF9", "#2E4BF9", "#2A4AF9", "#294BF9", "#274AF9", "#244BF9", "#234AF9", "#234BF9",
        "#234AF8", "#214AF9", "#224BF9", "#1F4AF9", "#1D4BF9", "#1B4BF9", "#184AF9", "#164BF9",
        "#134AF9", "#0C4AF9", "#074CF9", "#0753F9", "#0859F9", "#065EF9", "#0564F9", "#076BF9",
        "#0770F9", "#0777F9", "#067DF9", "#0783F9", "#0689F9", "#088FFA", "#1695FA", "#1F9BFA",
        "#27A2FB", "#2DA8FB", "#33AEFB", "#38B4FB", "#3DBAFB", "#42C0FB", "#46C6FB", "#4BCCFB",
        "#4FD2FC", "#53D8FC", "#57DEFC", "#5CE5FD", "#5FEAFC", "#64F1FD", "#68F7FE", "#69FAFE",
        "#69F9FB", "#6AF9F6", "#6BF9F0", "#6CF8EA", "#6CF8E4", "#6CF7DE", "#6DF7D7", "#6EF7D2",
        "#6EF6CB", "#6FF6C6", "#70F6C0", "#70F6BA", "#71F5B3", "#71F5AD", "#71F4A7", "#72F4A2",
        "#73F59C", "#73F496", "#73F48F", "#73F48A", "#74F484", "#74F37E", "#74F377", "#74F371",
        "#74F26B", "#75F365", "#75F25F", "#75F259", "#76F253", "#76F24D", "#76F246", "#76F240",
        "#76F23A", "#76F13A", "#77F23B", "#76F23A", "#76F23A", "#76F23B", "#76F23A", "#77F23B",
        "#76F23A", "#76F23A", "#77F23B", "#76F13A", "#76F23A", "#76F13A", "#76F23A", "#76F23A",
        "#77F23B", "#77F23B", "#76F23A", "#77F23B", "#76F13A", "#76F23B", "#76F13A", "#76F23B",
        "#76F23A", "#76F23A", "#76F23B", "#76F23A", "#77F23B", "#76F23A", "#76F23A", "#76F23A",
        "#7DF23B", "#84F23B", "#8AF33B", "#91F33C", "#97F33C", "#9DF33C", "#A3F33C", "#A9F33C",
        "#B0F33D", "#B6F43E", "#BDF43E", "#C2F43E", "#C9F43E", "#CFF53F", "#D6F540", "#DBF53F",
        "#E1F641", "#E8F641", "#EEF642", "#F4F642", "#FAF742", "#FDF742", "#FFF642", "#FFF141",
        "#FFEA3F", "#FFE43E", "#FFDF3D", "#FED83C", "#FDD23A", "#FCCC39", "#FBC638", "#FAC036",
        "#FABB35", "#F8B433", "#F7AE32", "#F6A730", "#F6A22F", "#F69B2E", "#F4952C", "#F48F2B",
        "#F48A2B", "#F38329", "#F27D28", "#F27627", "#F27026", "#F16A25", "#F06423", "#F05D23",
        "#F05822", "#EF5121", "#EF4B20", "#EF4520", "#EF3E1F", "#EF3E1F", "#EF3D1F", "#EE3D1F",
        "#EF3D1F", "#EF3D1F", "#EF3E1F", "#EF3D1F", "#EE3D1F", "#EE3D1F", "#EE3D1E", "#EE3D1F"
    )
    
    private val eachHeight by lazy { heightF / colorPadData.size }
    private var listener: OnHSEChangeListener? = null
    
    init {
        setOnTouchListener { _, event ->
            if (event.action in (ACTION_DOWN .. ACTION_MOVE)) {
                updateTouch(event.y)
            }
            return@setOnTouchListener true
        }
    }
    
    private fun updateTouch(y: Float) {
        when {
            y < 0 -> {
                touchY = 0F
                listener?.onHSEChange(0F)
            }
            y > heightF -> {
                touchY = heightF
                listener?.onHSEChange(360F - 360F / heightF * (heightF - 0.001F))
            }
            else -> {
                touchY = y
                listener?.onHSEChange(360F - 360F / heightF * y)
            }
        }
        invalidate()
    }
    
    fun setOnHSEChangeListener(listener: OnHSEChangeListener?) {
        this.listener = listener
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        
        // Draw colors
        repeat(colorPadData.size) { index ->
            paint.color = Color.parseColor(colorPadData[index])
            paint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawRect(0F, eachHeight * index, widthF, eachHeight * (index + 1), paint)
        }
        // Draw pointer
        canvas.drawBitmap(pointer, pointerX, touchY - pointer.height / 2, paint)
    }
    
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
    
}