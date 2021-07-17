package sakuraba.saki.list.launcher.main.setting

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import lib.github1552980358.ktExtension.jvm.keyword.tryRun
import sakuraba.saki.list.launcher.base.SettingValueChangeListener
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.booleanKeys
import sakuraba.saki.list.launcher.main.setting.SettingContainer.Companion.stringKeys
import java.io.Serializable

class SettingContainer(context: Context): Serializable {
    
    companion object {
        const val SETTING_CONTAINER = "SettingContainer"
        
        const val KEY_USE_FINGERPRINT = "key_use_fingerprint"   // Boolean
        const val KEY_USE_PIN = "key_use_pin"
        const val KEY_PIN_CODE = "key_pin_code"
        const val KEY_CUSTOM_STATUS_BAR_BLACK_TEXT = "key_status_bar_black_text"
        const val KEY_CUSTOM_STATUS_BAR_COLOR = "key_custom_status_bar_color"
        const val KEY_STATUS_BAR_COLOR = "key_status_bar_color"
        const val KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR = "key_custom_toolbar_background_color"
        const val KEY_TOOLBAR_BACKGROUND_COLOR = "key_toolbar_background_color"
        const val KEY_CUSTOM_BACKGROUND_IMAGE = "key_custom_background_image"
        const val KEY_BACKGROUND_IMAGE = "key_background_image"
        const val KEY_CUSTOM_TITLE_COLOR = "key_custom_title_color"
        const val KEY_TITLE_COLOR = "key_title_color"
        const val KEY_CUSTOM_SUMMARY_COLOR = "key_custom_summary_color"
        const val KEY_SUMMARY_COLOR = "key_summary_color"
        const val KEY_CUSTOM_NAVIGATION_BAR_COLOR = "key_custom_navigation_bar_color"
        const val KEY_NAVIGATION_BAR_COLOR = "key_navigation_bar_color"
        const val KEY_USE_SYSTEM_BACKGROUND = "key_use_system_background"
        const val KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_NORMAL = "key_quick_access_button_icon_color_normal"
        const val KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_CLICKED = "key_quick_access_button_icon_color_clicked"
        
        val booleanKeys = arrayOf(
            KEY_CUSTOM_STATUS_BAR_COLOR,
            KEY_CUSTOM_STATUS_BAR_BLACK_TEXT,
            KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR,
            KEY_CUSTOM_BACKGROUND_IMAGE,
            KEY_CUSTOM_TITLE_COLOR,
            KEY_CUSTOM_SUMMARY_COLOR,
            KEY_CUSTOM_NAVIGATION_BAR_COLOR,
            KEY_USE_SYSTEM_BACKGROUND
        )
        
        val stringKeys = arrayOf(
            KEY_STATUS_BAR_COLOR,
            KEY_TOOLBAR_BACKGROUND_COLOR,
            KEY_TITLE_COLOR,
            KEY_SUMMARY_COLOR,
            KEY_NAVIGATION_BAR_COLOR,
            KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_NORMAL,
            KEY_QUICK_ACCESS_BUTTON_ICON_COLOR_CLICKED
        )
    }
    
    private val stringMap = mutableMapOf<String, String?>()
    private val booleanMap = mutableMapOf<String, Boolean>()
    private val settingValueListeners = arrayListOf<SettingValueChangeListener>()
    
    init {
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
    
        /**
         * Important for security
         **/
        if (preferenceManager.contains(KEY_USE_PIN)) {
            booleanMap[KEY_USE_PIN] = preferenceManager.getBoolean(KEY_USE_PIN, false)
        }
        if (preferenceManager.contains(KEY_USE_FINGERPRINT)) {
            booleanMap[KEY_USE_FINGERPRINT] = preferenceManager.getBoolean(KEY_USE_FINGERPRINT, false)
        }
        if (preferenceManager.contains(KEY_PIN_CODE)) {
            stringMap[KEY_PIN_CODE] = preferenceManager.getString(KEY_PIN_CODE, null)
        }
        
        booleanKeys.forEach { key ->
            if (preferenceManager.contains(key)) {
                booleanMap[key] = preferenceManager.getBoolean(key, false)
            }
        }
        stringKeys.forEach { key ->
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

fun SharedPreferences.Editor.getResetSharedPreferenceEditor(): SharedPreferences.Editor {
    // Boolean
    // putBoolean(KEY_CUSTOM_STATUS_BAR_COLOR, false)
    // putBoolean(KEY_CUSTOM_STATUS_BAR_BLACK_TEXT, false)
    // putBoolean(KEY_CUSTOM_TOOLBAR_BACKGROUND_COLOR, false)
    // putBoolean(KEY_CUSTOM_BACKGROUND_IMAGE, false)
    // putBoolean(KEY_CUSTOM_TITLE_COLOR, false)
    // putBoolean(KEY_CUSTOM_SUMMARY_COLOR, false)
    // putBoolean(KEY_CUSTOM_NAVIGATION_BAR_COLOR, false)
    // putBoolean(KEY_USE_SYSTEM_BACKGROUND, false)
    
    booleanKeys.forEach { key -> putBoolean(key, false) }
    
    // Strings
    // putString(KEY_STATUS_BAR_COLOR, DEFAULT_STATUS_BAR_COLOR)
    // putString(KEY_TOOLBAR_BACKGROUND_COLOR, DEFAULT_TOOLBAR_BACKGROUND_COLOR)
    // putString(KEY_TITLE_COLOR, DEFAULT_TITLE_COLOR)
    // putString(KEY_SUMMARY_COLOR, DEFAULT_SUMMARY_COLOR)
    // putString(KEY_NAVIGATION_BAR_COLOR, DEFAULT_NAVIGATION_BAR_COLOR)
    
    stringKeys.forEach { key -> remove(key) }
    
    return this
}