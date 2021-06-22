package sakuraba.saki.list.launcher.view.base

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

open class BaseView: View {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    protected val heightFloat get() = height.toFloat()
    protected val widthFloat get() = width.toFloat()
    protected val paint = Paint()
    
}