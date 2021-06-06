package sakuraba.saki.list.launcher.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.promeg.pinyinhelper.Pinyin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sakuraba.saki.list.launcher.BuildConfig
import sakuraba.saki.list.launcher.R
import sakuraba.saki.list.launcher.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    
    private lateinit var homeViewModel: HomeViewModel
    private var _fragmentHomeBinding: FragmentHomeBinding? = null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // val root = inflater.inflate(R.layout.fragment_home, container, false)
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater)
        return fragmentHomeBinding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        homeViewModel.setAppInfos(arrayListOf())
        val appInfos = homeViewModel.appInfos.value!!
    
        @Suppress("EXPERIMENTAL_API_USAGE")
        GlobalScope.launch(Dispatchers.IO) {
            requireContext().packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }, 0).forEach { resolveInfo ->
                if (resolveInfo.resolvePackageName != BuildConfig.APPLICATION_ID) {
                    appInfos.add(
                        AppInfo(
                            resolveInfo.loadLabel(requireContext().packageManager).toString(),
                            resolveInfo.activityInfo.packageName,
                            resolveInfo.loadIcon(requireContext().packageManager)
                        )
                    )
                }
            }
        
            appInfos.sortBy { Pinyin.toPinyin(it.name, "") }
        
            launch(Dispatchers.Main) {
                fragmentHomeBinding.recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    
        // val appInfos = requireContext().packageManager.queryIntent.getInstalledApplications(0)
        fragmentHomeBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fragmentHomeBinding.recyclerView.adapter = RecyclerViewAdapter(appInfos, requireActivity().packageManager)
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentHomeBinding = null
        homeViewModel.setAppInfos(null)
    }
    
}