package sakuraba.saki.list.launcher.preference

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import sakuraba.saki.list.launcher.R

class ApplicationIconPreference(context: Context, attributeSet: AttributeSet?): Preference(context, attributeSet) {
    
    private var applicationIcon: Drawable? = null
    
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        (holder?.findViewById(R.id.image_view) as ImageView).setImageDrawable(applicationIcon)
    }
    
    fun setApplicationIcon(applicationIcon: Drawable) {
        this.applicationIcon = applicationIcon
    }
    
}