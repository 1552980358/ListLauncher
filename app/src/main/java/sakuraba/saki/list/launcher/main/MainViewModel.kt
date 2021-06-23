package sakuraba.saki.list.launcher.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sakuraba.saki.list.launcher.broadcast.ApplicationChangeBroadcastReceiver
import sakuraba.saki.list.launcher.main.setting.SettingContainer

class MainViewModel: ViewModel() {
    
    private val _settingContainer = MutableLiveData<SettingContainer>()
    fun setSettingContainer(settingContainer: SettingContainer?) {
        _settingContainer.value = settingContainer
    }
    val settingContainer get() = _settingContainer as LiveData<SettingContainer>
    
    private val _applicationChangeBroadcastReceiver = MutableLiveData<ApplicationChangeBroadcastReceiver>()
    fun setApplicationChangeBroadcastReceiver(receiver: ApplicationChangeBroadcastReceiver?) {
        _applicationChangeBroadcastReceiver.value = receiver
    }
    val applicationChangeBroadcastReceiver get() = _applicationChangeBroadcastReceiver as LiveData<ApplicationChangeBroadcastReceiver>
    
}