package sakuraba.saki.list.launcher.main.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingViewModel: ViewModel() {
    
    private val _settingContainer = MutableLiveData<SettingContainer>()
    fun setSettingContainer(settingContainer: SettingContainer?) {
        _settingContainer.value = settingContainer
    }
    val settingContainer = _settingContainer as LiveData<SettingContainer>
    
}