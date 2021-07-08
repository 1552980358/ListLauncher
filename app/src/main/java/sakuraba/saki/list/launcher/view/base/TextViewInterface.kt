package sakuraba.saki.list.launcher.view.base

import androidx.annotation.ColorInt

interface TextViewInterface {
    
    fun hasCustomTitleTextColor(): Boolean
    
    @ColorInt
    fun getTitleTextColor(): Int
    
    val titleTextViewColor get() = getTitleTextColor()
    
    fun hasCustomSummaryTextColor(): Boolean
    
    @ColorInt
    fun getSummaryTextColor(): Int
    
    val summaryTextViewColor get() = getSummaryTextColor()
    
}