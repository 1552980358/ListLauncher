package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import lib.github1552980358.ktExtension.android.view.heightF
import lib.github1552980358.ktExtension.android.view.widthF
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.view.base.BaseArrowView

class ColorTransparencyView: BaseArrowView {
    
    companion object {
        fun interface OnColorTransparencyChangeListener {
            fun onColorUpdate(@ColorInt color: Int, alpha: Int)
        }
    }
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private var color = Color.WHITE
    private var transparency = 1F
    private var listener: OnColorTransparencyChangeListener? = null
    private val bitmapShader by lazy {
        BitmapShader(
            resources.getDrawable(R.drawable.img_color_cube, null).toBitmap(),
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )
    }
    
    init {
        @Suppress("ClickableViewAccessibility")
        setOnTouchListener { _, event ->
            if (event.action in (ACTION_DOWN .. ACTION_MOVE)) {
                touchY = when {
                    event.y < 0 -> 0F
                    event.y > heightF -> heightF
                    else -> event.y
                }
                invalidate()
                updateTransparency(
                    when {
                        event.y < 0 -> 1F
                        event.y > height -> 0F
                        else -> 1F - event.y / heightF
                    }
                )
            }
            return@setOnTouchListener true
        }
    }
    
    fun setOnColorTransparencyChangeListener(listener: OnColorTransparencyChangeListener?) {
        this.listener = listener
    }
    
    fun updateColor(@ColorInt color: Int) {
        this.color = Color.parseColor("#" + String.format("%08X", color).substring(2, 8))
        invalidate()
        listener?.onColorUpdate(getColorInt(), (transparency * 255).toInt())
    }
    
    @ColorInt
    private fun getColorInt(): Int =
        Color.parseColor((String.format("#%02X", (transparency * 255).toInt()) + String.format("%08X", color).substring(2, 8)))
    
    private fun updateTransparency(transparency: Float) {
        this.transparency = transparency
        listener?.onColorUpdate(getColorInt(), (transparency * 255).toInt())
    }
    
    fun updateAlpha(alpha: Int) {
        updateTransparency(alpha / 255F)
        touchY = (1 - transparency) * heightF
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        
        // Background
        // 背景
        paint.shader = bitmapShader
        canvas.drawRect(0F, 0F, widthF, heightF, paint)
        
        // Color
        // 颜色
        @Suppress("DrawAllocation")
        paint.shader = LinearGradient(0F, 0F, widthF, heightF, color, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        canvas.drawRect(0F, 0F, widthF, heightF, paint)
        
        // Pointer
        // 指针
        canvas.drawBitmap(pointer, pointerX, touchY - pointer.height / 2, paint)
    }
    
    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
    
}