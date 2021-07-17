package sakuraba.saki.list.launcher.view.base

import android.content.Context
import android.util.AttributeSet
import androidx.core.graphics.drawable.toBitmap
import lib.github1552980358.ktExtension.android.view.widthF
import sakuraba.saki.list.launcher.R

open class BaseArrowView: BaseView {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    protected var touchY = 0F
    protected val pointer by lazy { resources.getDrawable(R.drawable.img_pointer, null).toBitmap() }
    protected val pointerX by lazy { widthF - pointer.width }
    
}