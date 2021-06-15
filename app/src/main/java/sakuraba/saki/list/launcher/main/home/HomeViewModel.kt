package sakuraba.saki.list.launcher.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    
    private val _appInfos = MutableLiveData<ArrayList<AppInfo>>()
    fun setAppInfos(arrayList: ArrayList<AppInfo>?) {
        _appInfos.value = arrayList
    }
    val appInfos get() = _appInfos as LiveData<ArrayList<AppInfo>>
    
}