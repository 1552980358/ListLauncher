package sakuraba.saki.list.launcher.view

import android.content.Context
import android.util.AttributeSet
import sakuraba.saki.list.launcher.view.base.SynchronizedTextView
import sakuraba.saki.list.launcher.view.base.TextViewInterface

class TitleTextView: SynchronizedTextView {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    override fun hasSyncTextColor() = (context as TextViewInterface).isCustomTitleColor
    
    override fun getSyncTextColor() = (context as TextViewInterface).customTitleColor
    
}