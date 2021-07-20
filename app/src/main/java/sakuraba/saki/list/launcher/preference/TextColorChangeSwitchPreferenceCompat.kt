package sakuraba.saki.list.launcher.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import sakuraba.saki.list.launcher.view.base.TextViewInterface

open class TextColorChangeSwitchPreferenceCompat: SwitchPreferenceCompat {
    
    private val iconTintChange get() = iconTintChange()
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        (context as TextViewInterface).apply {
            if (isCustomTitleColor) {
                holder?.itemView?.findViewById<TextView>(android.R.id.title)?.setTextColor(customTitleColor)
                if (iconTintChange && icon != null) {
                    icon.setTint(customTitleColor)
                }
            }
            if (isCustomSummaryColor) {
                holder?.itemView?.findViewById<TextView>(android.R.id.summary)?.setTextColor(customSummaryColor)
            }
        }
    }
    
    open fun iconTintChange() = true
    
}