package sakuraba.saki.list.launcher.main.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sakuraba.saki.list.launcher.main.launchApp.AuthorizationListener

class PinAuthorizeViewModel: ViewModel() {
    
    private val _settingContainer = MutableLiveData<SettingContainer>()
    fun setSettingContainer(settingContainer: SettingContainer?) {
        _settingContainer.value = settingContainer
    }
    val settingContainer get() = _settingContainer as LiveData<SettingContainer>
    
    private var _authorizationListener = MutableLiveData<AuthorizationListener>()
    fun setAuthorizationListener(authorizationListener: AuthorizationListener?) {
        _authorizationListener.value = authorizationListener
    }
    val authorizationListener get() = _authorizationListener as LiveData<AuthorizationListener>
    
    private val _pin = MutableLiveData<String>().apply { value = "" }
    fun pinAdd(char: Char) {
        _pin.value += char
    }
    fun pinRemove() {
        if (_pin.value!!.isNotEmpty()) {
            _pin.value = _pin.value!!.substring(0, _pin.value!!.lastIndex)
        }
    }
    val pinLength get() = _pin.value!!.length
    val pin get() = _pin as LiveData<String>
    
}