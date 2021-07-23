package sakuraba.saki.list.launcher.base

import sakuraba.saki.list.launcher.main.setting.SettingContainer
import java.io.Serializable

abstract class SettingValueChangeListener: Serializable {
    
    private var _settingContainer: SettingContainer? = null
    private val arrayList = arrayListOf<String>()
    
    constructor(): super()
    
    constructor(settingContainer: SettingContainer?): this() {
        setSettingContainer(settingContainer)
    }
    
    constructor(settingContainer: SettingContainer? = null, listenValues: List<String>): this(settingContainer) {
        listenValues.forEach { listenValue -> arrayList.add(listenValue) }
    }
    
    constructor(settingContainer: SettingContainer? = null, listenValue: String): this(settingContainer, listOf(listenValue))
    
    open fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: Boolean?) {}
    
    open fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: String?) {}
    
    fun setSettingContainer(settingContainer: SettingContainer?) {
        this._settingContainer = settingContainer
        _settingContainer?.addSettingValueChangeListener(this)
    }
    
    fun removeListener() {
        _settingContainer?.removeSettingValueChangeListener(this)
    }
    
    fun notifyUpdate(key: String, newValue: String?) {
        if (arrayList.contains(key)) {
            onSettingValueChange(_settingContainer, key, newValue)
        }
    }
    
    fun notifyUpdate(key: String, newValue: Boolean?) {
        if (arrayList.contains(key)) {
            onSettingValueChange(_settingContainer, key, newValue)
        }
    }
    
}

fun stringSettingValueChangeListener(
    settingContainer: SettingContainer? = null,
    listenValue: String,
    block: SettingValueChangeListener.(SettingContainer?, String, String?) -> Unit) =
    object : SettingValueChangeListener(settingContainer, listenValue) {
        override fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: String?) = this.block(settingContainer, key, newValue)
    }

fun booleanSettingValueChangeListener(
    settingContainer: SettingContainer? = null,
    listenValue: String,
    block: SettingValueChangeListener.(SettingContainer?, String, Boolean?) -> Unit) =
    object : SettingValueChangeListener(settingContainer, listenValue) {
        override fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: Boolean?) = this.block(settingContainer, key, newValue)
    }

fun stringSettingValueChangeListener(
    settingContainer: SettingContainer? = null,
    listenValues: List<String>,
    block: SettingValueChangeListener.(SettingContainer?, String, String?) -> Unit) =
    object : SettingValueChangeListener(settingContainer, listenValues) {
        override fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: String?) = this.block(settingContainer, key, newValue)
    }

fun booleanSettingValueChangeListener(
    settingContainer: SettingContainer? = null,
    listenValues: List<String>,
    block: SettingValueChangeListener.(SettingContainer?, String, Boolean?) -> Unit) =
    object : SettingValueChangeListener(settingContainer, listenValues) {
        override fun onSettingValueChange(settingContainer: SettingContainer?, key: String, newValue: Boolean?) = this.block(settingContainer, key, newValue)
    }