package sakuraba.saki.list.launcher.preference

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import sakuraba.saki.list.launcher.MainActivity
import sakuraba.saki.list.launcher.view.base.TextViewInterface

/***
 * Because unknown reason, changing title text color may cause the disappear of the whole [PreferenceCategory]
 ***/

class TextColorChangePreferenceCategory: PreferenceCategory {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
    
        (context as TextViewInterface).apply {
            Log.e("CONTEXT", (context is MainActivity).toString())
            if (hasCustomTitleTextColor()) {
                (holder?.findViewById(android.R.id.title) as TextView?)?.setTextColor(getTitleTextColor())
            }
        }
        
    }
    
}