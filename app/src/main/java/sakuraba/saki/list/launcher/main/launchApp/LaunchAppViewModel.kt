package sakuraba.saki.list.launcher.main.launchApp

import android.os.CancellationSignal
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
    
    private val _cancellationSignal = MutableLiveData<CancellationSignal>()
    fun setCancellationSignal(onCancelListener: CancellationSignal.OnCancelListener) {
        _cancellationSignal.value = CancellationSignal().apply { setOnCancelListener(onCancelListener) }
    }
    val cancellationSignal get() = _cancellationSignal as LiveData<CancellationSignal>
    
}