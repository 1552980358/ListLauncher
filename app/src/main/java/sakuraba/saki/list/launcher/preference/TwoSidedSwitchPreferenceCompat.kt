package sakuraba.saki.list.launcher.preference

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import sakuraba.saki.list.launcher.R

class TwoSidedSwitchPreferenceCompat: TextColorChangeSwitchPreferenceCompat {
    
    constructor(context: Context): this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?): super(context, attributeSet)
    
    companion object {
        fun interface OnContentClickListener {
            fun onClick(preference: TwoSidedSwitchPreferenceCompat)
        }
    }
    
    private var listener: OnContentClickListener? = null
    
    fun setOnContentClickListener(listener: OnContentClickListener?) {
        this.listener = listener
    }
    
    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        
        holder?.itemView?.apply {
            setOnClickListener(null)
            isClickable = false
        }
        
        holder?.itemView?.findViewById<ViewGroup>(android.R.id.widget_frame)?.setOnClickListener(
            (Preference::class.java.getDeclaredField("mClickListener").apply { isAccessible = true }.get(this as Preference) as View.OnClickListener)
        )
        
        holder?.itemView?.findViewById<ViewGroup>(R.id.content_frame)?.setOnClickListener {
            listener?.onClick(this)
        }
        
    }
    
}