package sakuraba.saki.list.launcher.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sakuraba.saki.list.launcher.main.setting.SettingContainer

class MainViewModel: ViewModel() {
    
    private val _settingContainer = MutableLiveData<SettingContainer>()
    fun setSettingContainer(settingContainer: SettingContainer?) {
        _settingContainer.value = settingContainer
    }
    val settingContainer get() = _settingContainer as LiveData<SettingContainer>
    
}