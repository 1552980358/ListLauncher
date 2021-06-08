package sakuraba.saki.list.launcher.main.setting

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import lib.github1552980358.ktExtension.jvm.keyword.tryRun
import java.io.Serializable

class SettingContainer(context: Context): Serializable {
    
    companion object {
        const val SETTING_CONTAINER = "SettingContainer"
        
        const val KEY_USE_FINGERPRINT = "key_use_fingerprint"   // Boolean
    }
    
    private val stringMap = mutableMapOf<String, String>()
    private val booleanMap = mutableMapOf<String, Boolean>()
    
    init {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
        booleanMap[KEY_USE_FINGERPRINT] = preferenceManager.getBoolean(KEY_USE_FINGERPRINT, false)
    }
    
    fun <T> getValue(key: String): T? {
        if (stringMap[key] != null) {
            return tryRun {
                @Suppress("UNCHECKED_CAST")
                stringMap[key] as T
            }
        }
        if (booleanMap[key] != null) {
            return tryRun {
                @Suppress("UNCHECKED_CAST")
                booleanMap[key] as T
            }
        }
        return null
    }
    
    fun getStringValue(key: String): String? = getValue(key)
    
    fun getBooleanValue(key: String): Boolean? = getValue(key)
    
    fun getStringUpdate(key: String, newValue: String) {
        stringMap[key] = newValue
    }
    
    fun getBooleanUpdate(key: String, newValue: Boolean) {
        booleanMap[key] = newValue
    }
    
}
