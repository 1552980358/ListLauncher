package sakuraba.saki.list.launcher.preference

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import sakuraba.saki.list.launcher.view.base.TextViewInterface

class TextColorChangePreference: Preference {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        (context as TextViewInterface).apply {
            if (hasCustomTitleColor()) {
                (holder?.findViewById(android.R.id.title) as TextView?)?.apply {
                    setTextColor(titleColor)
                    // Identification as category title
                    setTypeface(typeface, Typeface.BOLD)
                }
                if (icon != null) {
                    icon.setTint(titleColor)
                }
            }
            if (hasCustomSummaryColor()) {
                (holder?.findViewById(android.R.id.summary) as TextView?)?.apply {
                    setTextColor(summaryColor)
                }
            }
        }
    }
    
}