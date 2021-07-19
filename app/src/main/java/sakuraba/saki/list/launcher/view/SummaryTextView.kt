package sakuraba.saki.list.launcher.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import sakuraba.saki.list.launcher.view.base.TextViewInterface

class SummaryTextView: AppCompatTextView {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    init {
        getColorSync()
    }
    
    fun getColorSync() {
        if ((context as TextViewInterface).isCustomSummaryColor) {
            setTextColor((context as TextViewInterface).summaryColor)
        }
    }
    
}