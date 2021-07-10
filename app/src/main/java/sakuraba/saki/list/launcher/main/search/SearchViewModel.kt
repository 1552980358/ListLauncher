package sakuraba.saki.list.launcher.main.search

import androidx.lifecycle.ViewModel
import sakuraba.saki.list.launcher.main.home.AppInfo

class SearchViewModel: ViewModel() {

    private var _appInfos: ArrayList<AppInfo>? = null
    fun setAppInfos(arrayList: ArrayList<AppInfo>?) {
        _appInfos = arrayList
    }
    val appInfos get() = _appInfos!!
    
    val searchResult = arrayListOf<AppInfo>()

}