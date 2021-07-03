package sakuraba.saki.list.launcher.view.base

import androidx.annotation.ColorInt

interface TextViewInterface {
    
    fun hasCustomTitleTextColor(): Boolean
    
    @ColorInt
    fun getTitleTextColor(): Int
    
    fun hasCustomSummaryTextColor(): Boolean
    
    @ColorInt
    fun getSummaryTextColor(): Int
    
}