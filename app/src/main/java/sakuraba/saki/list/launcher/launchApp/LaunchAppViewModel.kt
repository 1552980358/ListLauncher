package sakuraba.saki.list.launcher.launchApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaunchAppViewModel: ViewModel() {
    
    private val _applicationName = MutableLiveData<String>()
    fun setApplicationName(applicationName: String? = null) {
        _applicationName.value = applicationName
    }
    val applicationName get() = _applicationName as LiveData<String>

    private val _packageName = MutableLiveData<String>()
    fun setPackageName(packageName: String? = null) {
        _packageName.value = packageName
    }
    val packageName get() = _packageName as LiveData<String>
    
}