package sakuraba.saki.list.launcher.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sakuraba.saki.list.launcher.main.setting.SettingContainer

class HomeViewModel: ViewModel() {
    
    private val _settingContainer = MutableLiveData<SettingContainer>()
    fun setSettingContainer(settingContainer: SettingContainer?) {
        _settingContainer.value = settingContainer
    }
    val settingContainer get() = _settingContainer as LiveData<SettingContainer>
    
    private val _appInfos = MutableLiveData<ArrayList<AppInfo>>()
    fun setAppInfos(arrayList: ArrayList<AppInfo>?) {
        _appInfos.value = arrayList
    }
    val appInfos get() = _appInfos as LiveData<ArrayList<AppInfo>>
    
}