package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import androidx.annotation.ColorInt
import lib.github1552980358.ktExtension.android.view.heightF
import lib.github1552980358.ktExtension.android.view.widthF
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.view.base.BaseView

class ColorPlateView: BaseView {
    
    companion object {
        
        fun interface OnColorChangeListener {
            fun onColorChange(@ColorInt color: Int)
        }
        
    }
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private val paintCircle = Paint().apply {
        strokeWidth = resources.getDimension(R.dimen.color_plate_circle_stroke)
        color = WHITE
        style = Paint.Style.STROKE
    }
    private val hsvData = floatArrayOf(1F, 1F, 1F)
    private val hsvTouch = floatArrayOf(1F, 1F, 1F)
    private val linearGradient by lazy { LinearGradient(0F, 0F, 0F, heightF, WHITE, BLACK, Shader.TileMode.CLAMP) }
    private var listener: OnColorChangeListener? = null
    private val circleRadius by lazy { resources.getDimension(R.dimen.color_plate_circle_radius) }
    
    private var touchX = 0F
    private var touchY = 0F
    
    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        @Suppress("ClickableViewAccessibility")
        setOnTouchListener { _, event ->
            if (event.action in (ACTION_DOWN .. ACTION_MOVE)) {
                when {
                    event.x < 0 -> {
                        hsvTouch[1] = 0F
                        touchX = 0F
                    }
                    event.x > width -> {
                        hsvTouch[1] = 1F
                        touchX = widthF
                    }
                    else -> {
                        hsvTouch[1] = 1 / widthF * event.x
                        touchX = event.x
                    }
                }
                when {
                    event.y < 0 -> {
                        hsvTouch[2] = 1F
                        touchY = 0F
                    }
                    event.y > height -> {
                        hsvTouch[2] = 1 - 1 / heightF * heightF
                        touchY = heightF
                    }
                    else -> {
                        hsvTouch[2] = 1 - 1 / heightF * event.y
                        touchY = event.y
                    }
                }
                invalidate()
                listener?.onColorChange(Color.HSVToColor(hsvTouch))
            }
            return@setOnTouchListener true
        }
    }
    
    fun setOnColorChangeListener(listener: OnColorChangeListener?) {
        this.listener = listener
    }
    
    fun setHSE(newHSE: Float) {
        hsvData[0] = newHSE
        hsvTouch[0] = newHSE
        invalidate()
        listener?.onColorChange(Color.HSVToColor(hsvTouch))
    }
    
    fun setHSV(colorStr: String) {
        Color.colorToHSV(Color.parseColor(colorStr), hsvTouch)
        hsvData[0] = hsvTouch[0]
        touchX = when {
            hsvTouch[1] == 0F -> 0F
            hsvTouch[1] == 1F -> widthF
            else -> hsvTouch[1] * widthF
        }
        touchY = when {
            hsvTouch[2] == 1F -> 0F
            else -> hsvTouch[2] + 1 - 1 / heightF
        }
        invalidate()
        listener?.onColorChange(Color.parseColor(colorStr))
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        touchX = MeasureSpec.getSize(widthMeasureSpec).toFloat()
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        @Suppress("DrawAllocation")
        paint.shader = ComposeShader(
                linearGradient,
                LinearGradient(0F, 0F, widthF, 0F, WHITE, Color.HSVToColor(hsvData), Shader.TileMode.CLAMP),
                PorterDuff.Mode.MULTIPLY
            )
        canvas.drawRect(0F, 0F, widthF, heightF, paint)
        canvas.drawCircle(touchX, touchY, circleRadius, paintCircle)
    }
    
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
    
}