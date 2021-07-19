package sakuraba.saki.list.launcher.view.base

import androidx.annotation.ColorInt

interface TextViewInterface {
    
    fun hasCustomTitleColor(): Boolean
    
    @ColorInt
    fun getTitleTextColor(): Int
    
    val titleColor get() = getTitleTextColor()
    
    fun hasCustomSummaryColor(): Boolean
    
    @ColorInt
    fun getSummaryTextColor(): Int
    
    val summaryColor get() = getSummaryTextColor()
    
}