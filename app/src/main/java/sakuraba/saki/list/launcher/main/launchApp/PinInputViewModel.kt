package sakuraba.saki.list.launcher.main.launchApp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sakuraba.saki.list.launcher.dialog.PinInputDialogFragment
import sakuraba.saki.list.launcher.main.setting.SettingContainer

class PinInputViewModel: ViewModel() {
    
    companion object {
        const val CLEAR = ' '
        const val CLEAR_ALL = '-'
    }
    
    private val _pinCode = MutableLiveData<String>().apply { value = "" }
    fun newKeyInput(char: Char) {
        Log.e("newKeyInput", "newKeyInput")
        when {
            char == CLEAR -> {
                if (_pinCode.value!!.isNotEmpty()) {
                    _pinCode.value = _pinCode.value!!.substring(0, _pinCode.value!!.length - 1)
                }
            }
            char == CLEAR_ALL -> {
                _pinCode.value = ""
            }
            _pinCode.value!!.length != PinInputDialogFragment.PIN_MAX_SIZE -> {
                _pinCode.value += char
            }
        }
    }
    val pinCode get() = _pinCode as LiveData<String>
    
    private val _settingContainer = MutableLiveData<SettingContainer>()
    fun setSettingContainer(settingContainer: SettingContainer?) {
        _settingContainer.value = settingContainer
    }
    val settingContainer get() = _settingContainer as LiveData<SettingContainer>
    
}