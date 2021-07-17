package sakuraba.saki.list.launcher.view

import android.content.Context
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.toBitmap
import lib.github1552980358.ktExtension.android.view.heightF
import lib.github1552980358.ktExtension.android.view.widthF
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.view.base.BaseView

class ColorPresentView: BaseView {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private val bitmapShader by lazy { BitmapShader(resources.getDrawable(R.drawable.img_color_cube, null).toBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT) }
    private val paintColor = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
    }
    
    init {
        paint.shader = bitmapShader
    }
    
    fun updateColor(@ColorInt color: Int) {
        paintColor.color = color
        invalidate()
    }
    
    val color get() = paintColor.color
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        // Draw background cubes
        // 绘制背景方格
        canvas.drawRect(0F, 0F, widthF, heightF, paint)
        
        // Draw color
        // 绘制颜色
        canvas.drawRect(0F, 0F, widthF, heightF, paintColor)
    }
    
}