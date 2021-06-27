package sakuraba.saki.list.launcher.main.setting

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import lib.github1552980358.ktExtension.jvm.keyword.tryRun
import sakuraba.saki.list.launcher.base.SettingValueChangeListener
import java.io.Serializable

class SettingContainer(context: Context): Serializable {
    
    companion object {
        const val SETTING_CONTAINER = "SettingContainer"
        
        const val KEY_USE_FINGERPRINT = "key_use_fingerprint"   // Boolean
        const val KEY_USE_PIN = "key_use_pin"
        const val KEY_PIN_CODE = "key_pin_code"
        const val KEY_EDIT_PIN = "key_edit_pin"
    }
    
    private val stringMap = mutableMapOf<String, String?>()
    private val booleanMap = mutableMapOf<String, Boolean>()
    private val settingValueListeners = arrayListOf<SettingValueChangeListener>()
    
    init {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
        arrayOf(KEY_USE_FINGERPRINT, KEY_USE_PIN).forEach { key ->
            if (preferenceManager.contains(key)) {
                booleanMap[key] = preferenceManager.getBoolean(key, false)
            }
        }
        arrayOf(KEY_PIN_CODE).forEach { key ->
            if (preferenceManager.contains(key)) {
                stringMap[key] = preferenceManager.getString(key, null)
            }
        }
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
    
    fun getStringUpdate(key: String, newValue: String?) {
        stringMap[key] = newValue
        settingValueListeners.forEach { listeners -> listeners.notifyUpdate(key, newValue) }
    }
    
    fun getBooleanUpdate(key: String, newValue: Boolean) {
        booleanMap[key] = newValue
        settingValueListeners.forEach { listeners -> listeners.notifyUpdate(key, newValue) }
    }
    
    @Synchronized
    fun addSettingValueChangeListener(listener: SettingValueChangeListener?) {
        if (listener != null) {
            settingValueListeners.add(listener)
        }
    }
    
    @Synchronized
    fun removeSettingValueChangeListener(listener: SettingValueChangeListener?) {
        if (settingValueListeners.contains(listener)) {
            settingValueListeners.remove(listener)
        }
    }
    
}
