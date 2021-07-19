package sakuraba.saki.list.launcher.view.base

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView

abstract class SynchronizedTextView: AppCompatTextView {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    private val isSyncTextColor get() = hasSyncTextColor()
    
    private val syncColor get() = getSyncTextColor()
    
    init {
        if (isSyncTextColor) {
            @Suppress("LeakingThis")
            setTextColor(syncColor)
        }
    }
    
    abstract fun hasSyncTextColor(): Boolean
    
    @ColorInt
    abstract fun getSyncTextColor(): Int
    
}