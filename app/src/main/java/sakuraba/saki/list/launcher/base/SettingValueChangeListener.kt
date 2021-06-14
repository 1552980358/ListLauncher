package sakuraba.saki.list.launcher.base

import sakuraba.saki.list.launcher.main.setting.SettingContainer

abstract class SettingValueChangeListener {
    
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