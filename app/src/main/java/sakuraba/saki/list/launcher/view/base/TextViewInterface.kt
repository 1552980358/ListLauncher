package sakuraba.saki.list.launcher.view.base

import androidx.annotation.ColorInt

interface TextViewInterface {
    
    fun hasCustomTitleColor(): Boolean
    
    val isCustomTitleColor get() = hasCustomTitleColor()
    
    @ColorInt
    fun getTitleTextColor(): Int
    
    val titleColor get() = getTitleTextColor()
    
    /*******************************************************/
    
    fun hasCustomSummaryColor(): Boolean
    
    val isCustomSummaryColor get() = hasCustomSummaryColor()
    
    @ColorInt
    fun getSummaryTextColor(): Int
    
    val summaryColor get() = getSummaryTextColor()
    
}